package com.amaan.service.impl;

import com.amaan.dao.SeckillDao;
import com.amaan.dao.SuccessKilledDao;
import com.amaan.domain.Seckill;
import com.amaan.domain.SuccessKilled;
import com.amaan.dto.Exposer;
import com.amaan.dto.SeckillExecution;
import com.amaan.enums.SeckillStatEnum;
import com.amaan.exception.RepeatKillException;
import com.amaan.exception.SeckillCloseException;
import com.amaan.exception.SeckillException;
import com.amaan.service.SeckillService;
import com.amaan.config.BloomFilterHelper;
import com.amaan.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author codingBoy
 * @date 20/11/02
 */
@Service("seckillService")
public class SeckillServiceImpl implements SeckillService {

    //日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出我们的md5值，值任意给，越复杂越好
     */
    private final String salt = "shsdssljdd'l.";
    private static final String PROD_PREFIX = "PROD::";
    private static final String BLOOM_KEY = "bloom::prod";

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private BloomFilterHelper<String> bloomFilterHelper;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        String prodKey = PROD_PREFIX+ seckillId;
        Seckill prod;
        if (redisUtils.exists(prodKey)){
            prod = new Seckill();
            prod.setSeckillId(seckillId);
            prod.setName((String) redisUtils.hmGet(prodKey,"name"));
            prod.setNumber(Integer.parseInt((String) redisUtils.hmGet(prodKey,"number")));
            try {
                prod.setStartTime(stringToDate((String) redisUtils.hmGet(prodKey,"start-time")));
                prod.setEndTime(stringToDate((String)redisUtils.hmGet(prodKey,"end-time")));
                return prod;
            } catch (ParseException e) {
                logger.error("日期转换失败");
                e.printStackTrace();
                redisUtils.remove(prodKey);
            }
        }
        prod = seckillDao.queryById(seckillId);
        if (prod!=null){
            redisUtils.hmSet(prodKey,"id",seckillId+"");
            redisUtils.hmSet(prodKey,"name",prod.getName());
            redisUtils.hmSet(prodKey,"number",prod.getNumber()+"");
            redisUtils.hmSet(prodKey,"start-time",dateToString(prod.getStartTime()));
            redisUtils.hmSet(prodKey,"end-time",dateToString(prod.getEndTime()));
        }
        return prod;
    }

    @Override
    public Seckill getByIdWithBloom(long seckillId){
        String prodKey = PROD_PREFIX+ seckillId;
        Seckill prod;
        //查询先看布隆过滤器里有没有，有就去查缓存，没有查数据库
        if(redisUtils.includeByBloomFilter(bloomFilterHelper,BLOOM_KEY,prodKey)) {
            if (redisUtils.exists(prodKey)){
                prod = new Seckill();
                prod.setSeckillId(seckillId);
                prod.setName((String) redisUtils.hmGet(prodKey,"name"));
                prod.setNumber(Integer.parseInt((String) redisUtils.hmGet(prodKey,"number")));
                try {
                    prod.setStartTime(stringToDate((String) redisUtils.hmGet(prodKey,"start-time")));
                    prod.setEndTime(stringToDate((String)redisUtils.hmGet(prodKey,"end-time")));
                    return prod;
                } catch (ParseException e) {
                    logger.error("日期转换失败");
                    e.printStackTrace();
                    redisUtils.remove(prodKey);
                }
            }
        }
        prod = seckillDao.queryById(seckillId);
        if (prod!=null){
            redisUtils.hmSet(prodKey,"id",seckillId+"");
            redisUtils.hmSet(prodKey,"name",prod.getName());
            redisUtils.hmSet(prodKey,"number",prod.getNumber()+"");
            redisUtils.hmSet(prodKey,"start-time",dateToString(prod.getStartTime()));
            redisUtils.hmSet(prodKey,"end-time",dateToString(prod.getEndTime()));
            //同时加入布隆过滤器
            redisUtils.addByBloomFilter(bloomFilterHelper,BLOOM_KEY,prodKey);
        }
        return prod;
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {

        Seckill seckill = getById(seckillId);

        //若是秒杀未开启
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统当前时间
        Date nowTime = new Date();
        if (startTime.getTime() > nowTime.getTime() || endTime.getTime() < nowTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        //秒杀开启，返回秒杀商品的id、用给接口加密的md5
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    private String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date);
    }
    private Date stringToDate(String s) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.parse(s);
    }

    //秒杀是否成功，成功:减库存，增加明细；失败:抛出异常，事务回滚
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    /**
     * 使用注解控制事务方法的优点:
     * 1.开发团队达成一致约定，明确标注事务方法的编程风格
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC/HTTP请求或者剥离到事务方法外部，因为rpc是不回滚的，如果事务回滚了会造成数据不一致
     * 3.不是所有的方法都需要事务，如只有一条修改操作、只读操作不要事务控制
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            //秒杀数据被重写了
            throw new SeckillException("seckill data rewrite");
        }
        //执行秒杀逻辑:减库存+增加购买明细
        Date nowTime = new Date();
        try {
            //先查库存，悲观锁
            if (seckillDao.queryNumById(seckillId)<=0){
                throw new SeckillCloseException("none lefts! seckill ends!");
            }
            //否则更新了库存，秒杀成功,增加明细
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            //看是否该明细被重复插入，即用户是否重复秒杀，主键冲突
            if (insertCount <= 0) {
                throw new RepeatKillException("seckill repeated");
            }
            //减库存,热点商品竞争
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //没有更新库存记录，说明秒杀结束 rollback
                throw new SeckillCloseException("prod beyond expire! seckill is closed");
            }
            //秒杀成功,得到成功插入的明细记录,并返回成功秒杀的信息 commit
            SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
            return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
        } catch (SeckillCloseException | RepeatKillException e1) {
            throw e1;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所以编译期异常转化为运行期异常
            throw new SeckillException("seckill inner error :" + e.getMessage());
        }
    }
}
