package init;

import org.beetl.core.GroupTemplate;
import org.beetl.ext.jfinal.BeetlRenderFactory;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

import controller.FileController;
import controller.VerifyAjaxController;
import controller.VerifyController;
import entity.User;

public class InitConfig extends JFinalConfig {

	/*
	 * 此方法用来配置 JFinal 常量值， 如开发模式常量 devMode 的配置， 默认视图类型 ViewType 的配置
	 */
	@Override
	public void configConstant(Constants me) {
		// 开启开发模式
		me.setDevMode(true);
		// 整合beetl模版
		me.setMainRenderFactory(new BeetlRenderFactory());
		// 获取GroupTemplate ,可以设置共享变量等操作
		GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate;
		// 设置静态资源路径
		me.setBaseViewPath("/views");
	}

	/*
	 * 此方法用来配置 JFinal 访问路由， 如下代码配置了将”/hello”映射到 HelloController 这个控 制 器
	 */
	@Override
	public void configRoute(Routes me) {
		//添加控制器路由
		me.add("/verification", VerifyController.class);
		me.add("/verifyAjax", VerifyAjaxController.class);
		me.add("/upload", FileController.class);

	}

	/*
	 * 此方法用来配置 JFinal 的 Plugin， 如下代码配置了 C3p0 数据库连接池插件与 ActiveRecord 数据库访问插件。
	 */
	@Override
	public void configPlugin(Plugins me) {
		// 配置数据源和jfinal的activeRecordPlugin的数据映射管理
		PropKit.use("jdbc.properties");
		C3p0Plugin cp = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
		me.add(cp);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		me.add(arp);
		// 后台显示sql语句
		arp.setShowSql(true);
		// 添加user_tb表映射
		arp.addMapping("user_tb", User.class);
	}

	/*
	 * 此方法用来配置 JFinal 的全局拦截器，全局拦截器将拦截所有 action 请求，除非使用
	 * 
	 * @Clear 在 Controller 中清除
	 * 
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		// TODO 自动生成的方法存根

	}

	/*
	 * 此方法用来配置JFinal的Handler， 如下代码配置了名为ResourceHandler的处理器， Handler 可以接管所有 web
	 * 请求，并对应用拥有完全的控制权，可以很方便地实现更高层的功能性扩 展 。
	 * 
	 */
	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler());

	}

}
