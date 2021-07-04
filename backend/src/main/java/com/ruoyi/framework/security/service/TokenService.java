package com.ruoyi.framework.security.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.IdUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.security.LoginUser;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token验证处理
 * 
 * @author ruoyi
 */
@Component
public class TokenService
{

    /**
     * 令牌自定义标识
     */
    @Value("${token.header}")
    private String header;

    /**
     * 令牌秘钥
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * 令牌有效期（默认30分钟）
     */
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取用户身份信息
     * 
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);
        // 如果获取到了token
        if (StringUtils.isNotEmpty(token))
        {
            // 解析token
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
            // 返回拼接后的uuid
            String userKey = getTokenKey(uuid);
            // 获取登录用户个人信息
            LoginUser user = redisCache.getCacheObject(userKey);
            return user;
        }
        // 没有获取到token则返回空
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser)
    {
        // 如果用户已登录且token不为空
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken()))
        {
            // 刷新令牌有效期
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token)
    {
        // 当前用户已经登录
        if (StringUtils.isNotEmpty(token))
        {
            // 获取token
            String userKey = getTokenKey(token);
            // 在Redis缓存中删除该用户的token对象
            redisCache.deleteObject(userKey);
        }
    }

    /**
     * 创建令牌
     * 
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser)
    {
        // 生成token（随机获取uuid）
        String token = IdUtils.fastUUID();
        // 设置用户token
        loginUser.setToken(token);
        // 设置用户代理
        setUserAgent(loginUser);
        // 刷新令牌有效期
        refreshToken(loginUser);

        // 创建一个HashMap
        Map<String, Object> claims = new HashMap<>();
        // 将令牌前缀和token放入map中（以key，value形式）
        claims.put(Constants.LOGIN_USER_KEY, token);
        // 返回已生成的令牌
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     * 
     * @param token 令牌
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser)
    {
        // 得到当前用户令牌的过期时间
        long expireTime = loginUser.getExpireTime();
        // 得到当前时间（毫秒）
        long currentTime = System.currentTimeMillis();
        // 如果令牌过期时间与当前时间之差小于令牌默认有效期（30min）
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            // 刷新当前登录用户的令牌有效期
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     * 
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser)
    {
        // 设置登录时间（返回当前时间，毫秒）
        loginUser.setLoginTime(System.currentTimeMillis());
        // 设置令牌失效时间
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 获取当前登录用户的token
        String userKey = getTokenKey(loginUser.getToken());
        // 根据uuid将loginUser缓存
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }
    
    /**
     * 设置用户代理信息
     * 
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        // 通过获取request请求得到ip
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        // 设置当前用户的ip地址
        loginUser.setIpaddr(ip);
        // 设置当前用户的登陆地点
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        // 设置当前用户的浏览器名称
        loginUser.setBrowser(userAgent.getBrowser().getName());
        // 设置当前用户的操作系统名称
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }
    
    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims)
    {
        // 生成令牌
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        // 返回已生成的令牌
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token)
    {
        // 解析token
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token)
    {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }

    /**
     * 获取请求token
     *
     * @param request 请求队对象
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        // 获取请求头作为token
        String token = request.getHeader(header);
        // 如果token不为空，且token的前缀为指定的"Bearer "
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX))
        {
            // 得到处理后的token（把令牌前缀替换为空）
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        // 返回结果
        return token;
    }

    /**
     * 获取uuid
     * @param uuid uuid
     * @return 拼接后的uuid
     */
    private String getTokenKey(String uuid)
    {
        // 返回拼接后的uuid
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }
}
