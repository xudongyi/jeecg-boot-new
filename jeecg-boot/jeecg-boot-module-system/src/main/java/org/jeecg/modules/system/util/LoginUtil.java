package org.jeecg.modules.system.util;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.vo.SysUserVo;

import java.util.List;

public class LoginUtil {

    /**
     * 用户信息
     *
     * @param sysUser
     * @param result
     * @return
     */
    public static Result<JSONObject> userInfo(SysUserVo sysUser, Result<JSONObject> result, RedisUtil redisUtil,
                                        ISysDepartService sysDepartService, ISysUserService sysUserService, ISysDictService sysDictService) {
        String syspassword = sysUser.getPassword();
        String username = sysUser.getUsername();
        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME*2 / 1000);

        // 获取用户部门信息
        JSONObject obj = new JSONObject();
        List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
        obj.put("departs", departs);
        if (departs == null || departs.size() == 0) {
            obj.put("multi_depart", 0);
        } else if (departs.size() == 1) {
            sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
            obj.put("multi_depart", 1);
        } else {
            obj.put("multi_depart", 2);
        }
        obj.put("token", token);
        obj.put("userInfo", sysUser);
        obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
        String a  = RandomUtil.randomString(10)+sysUser.getUsername()+RandomUtil.randomString(10);
        //随机数
        obj.put("tickit", a);
        redisUtil.set(a,JSONObject.toJSONString(sysUser),7*24*60*1000);//存一天
        result.setResult(obj);
        result.success("登录成功");
        return result;
    }

}
