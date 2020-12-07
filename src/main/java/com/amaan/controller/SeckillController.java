package com.amaan.controller;

import com.amaan.domain.Seckill;
import com.amaan.dto.Exposer;
import com.amaan.dto.SeckillExecution;
import com.amaan.dto.SeckillResult;
import com.amaan.enums.SeckillStatEnum;
import com.amaan.exception.RepeatKillException;
import com.amaan.exception.SeckillCloseException;
import com.amaan.service.SeckillService;
import com.amaan.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author amaan
 * @date 20/10/10
 */
@RestController
@RequestMapping("/seckill")//url:模块/资源/{}/动作GET、POST、PUT、DELETE直观地表示获取、创建/更新、更新和删除
public class SeckillController {

    private static final int REQUEST_SUCCESS = 0;
    private static final int REQUEST_FAILED = -1;
    private static final int REQUEST_INNER_ERROR = 10;

    @Autowired
    private SeckillService seckillService;

    /**
     * 跳转至商品列表页
     * public String list(Model model) {
     *     list.jsp+mode=ModelAndView
     *
     *         List<Seckill> list=seckillService.getSeckillList();
     *         model.addAttribute("list",list);
     *         return "list";
     *  }
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
//    @ResponseBody
    public List<Seckill> list(){
        return seckillService.getSeckillList();
    }

    /**
     * 跳转至商品详情页
     * 未完善，没有detail.jsp
     * 不符合REST，弃用
     * @param seckillId
     * @param model
     * @return
     */
    @Deprecated
//    @RequestMapping(value = "/{seckillId}/detail",method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";
        }

        Seckill seckill=seckillService.getById(seckillId);
        if (seckill==null) {
            return "forward:/seckill/list";
        }
        model.addAttribute("seckill",seckill);
        return "detail";
    }
    @GetMapping(value = "/detail")
//    @RequestBody
    public String detail(@RequestParam("seckillId") Long seckillId){
        Seckill seckill=seckillService.getById(seckillId);
        if (seckill==null){
            return JsonUtil.getJSONString(-1,"prod not found");
        }else{
            Map<String,Object> res = new HashMap<>();
            res.put(String.valueOf(seckillId),seckill);
            return JsonUtil.getJSONString(0,res);
        }
    }

    /**
     * ajax ,json暴露秒杀接口的方法
     * result的true/false代表请求是否成功，秒杀接口是否暴露的信息是在exposer里
     */
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result;
        try{
            Exposer exposer=seckillService.exportSeckillUrl(seckillId);
            //这个true不代表秒杀已经开启
            result= new SeckillResult<>(REQUEST_SUCCESS,"进行秒杀！",exposer);
        }catch (Exception e) {
            e.printStackTrace();
            result= new SeckillResult<>(REQUEST_INNER_ERROR,"请求失败", e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
//    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
                                                   @PathVariable("md5") String md5,
//                                                   @CookieValue(value = "userPhone",required = false) Long userPhone)
                                                   @RequestParam("userPhone") Long userPhone) {
        if (userPhone==null) {
            return new SeckillResult<>(REQUEST_FAILED,"未注册","用户未注册");
        }

        try {
            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
            return new SeckillResult<>(REQUEST_SUCCESS, "进行秒杀！", execution);
        }catch (RepeatKillException e1) {
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<>(REQUEST_SUCCESS,"重复秒杀", execution);
        }catch (SeckillCloseException e2) {
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<>(REQUEST_SUCCESS,"秒杀已结束", execution);
        } catch (Exception e) {
            SeckillExecution execution=new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<>(REQUEST_INNER_ERROR,"系统异常", execution);
        }

    }

    //获取系统时间
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
//    @ResponseBody
    public SeckillResult<Long> time() {
        Date now=new Date();
        return new SeckillResult<>(REQUEST_SUCCESS, "系统时间", now.getTime());
    }
}























