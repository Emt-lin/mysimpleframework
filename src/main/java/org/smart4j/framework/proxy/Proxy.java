package org.smart4j.framework.proxy;

/**
 * @author psl
 * @date 2020/7/7
 *  代理接口
 */
public interface Proxy {
    /**
     * 执行链式代理
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
