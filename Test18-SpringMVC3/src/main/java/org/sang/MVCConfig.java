package org.sang;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * Created by sang on 16-12-16.
 */
@Configuration
@EnableWebMvc
@ComponentScan("org.sang")
public class MVCConfig extends WebMvcConfigurerAdapter{
//    @Bean
//    public InternalResourceViewResolver viewResolver() {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/classes/views/");
//        viewResolver.setSuffix(".jsp");
//        viewResolver.setViewClass(JstlView.class);
//        return viewResolver;
//    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/views/",".jsp");//添加InternalResourceViewResolver视图解析器

        /*该方法会创建一个内容裁决解析器ContentNegotiatingViewResolver ，该解析器不进行具体视图的解析，
        而是管理你注册的所有视图解析器，所有的视图会先经过它进行解析，然后由它来决定具体使用哪个解析器进行解析。
        具体的映射规则是根据请求的media types来决定的*/
        registry.enableContentNegotiation(new MappingJackson2JsonView());//添加默认的视图解析器MappingJackson2JsonView
//        registry.viewResolver(); //注册视图解析器；自定义视图解析器
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true)   //是否通过请求URL的扩展名来决定media type
                .ignoreAcceptHeader(true)     //不检查accept请求头
                .parameterName("mediaType")
                .defaultContentType(MediaType.TEXT_HTML)  //设置默认的meida type
                .mediaType("html", MediaType.TEXT_HTML)   //请求已.html结尾的会被当成MediaType.TEXT_HTML
                .mediaType("json", MediaType.APPLICATION_JSON); //请求已.json结尾的会被当成MediaType.APPLICATION_JSON

    }

    /**
     * /**的意思是所有文件，包括文件夹中的子文件
     * /*是所有文件，不包含子文件
     * /是web项目的根目录
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //两个*表示以/assets开始的任意层级的路径都可以访问得到图片，如<img src="../assets/img/1.png">
        //一个*表示只可以访问assets目录下的图片文件
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/assets/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptors()).addPathPatterns("/**");//配置拦截器
    }
    @Bean
    public MyInterceptors myInterceptors() {
        return new MyInterceptors();
    }

    //快速解决页面转向问题
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/helloworld").setViewName("/hello");
        registry.addViewController("/world").setViewName("/world");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
}
