package controller;

import java.io.File;

import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

public class FileController extends Controller {
	public void index() {

	}

	public void uploadPage() {

		// 传入项目绝对路径给beetl
		setAttr("basePath", "/LoginModule2");
		// 跳转上传页面
		render("/uploadFile.html");
	}

	public void uploadFile() {

		// 使用jfinal自带的上传类加上cos.jar包实现上传功能
		// 没指定路径时,默认会存入名为控制器的路由名的文件夹里
		// 存放相同文件会在后面直接加数字序号
		//debug模式下的tomcat每改动一次就要重新部署项目,所有上次上传的东西会没有了
		UploadFile uploadFile = getFile(getPara("file"), "..\\uploadFile");
		renderText("上传成功");
	}

	public void downloadFile() {
		//获取web应用所部署的服务器的应用根目录
		String webUrl = PathKit.getWebRootPath();
//		System.out.println(webUrl);
		//获取传过来客户想下载的文件名
		String fileName = getPara("fileName");
		String fileUrl = webUrl + "\\uploadFile\\" + fileName;
//		System.out.println(fileUrl);
		//通过路径合成,得出文件所在服务器的真实路径,通过file获取文件的io
		File file = new File(fileUrl);
		//判断文件是否存在
		if (file.isFile()) {
			//返回文件
			renderFile(file);
//			System.out.println("找到文件");
		} else {
			//没有文件则提示下载失败
//			System.out.println("找不到文件");
			renderText("下载失败");
		}

	}

}
