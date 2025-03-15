package Client.View;

import org.apache.log4j.Logger;
import org.example.Main;

/**
 * @author xiezhr
 * @create 2024-04-27 10:45
 */
public class UI {
    Logger logger = Logger.getLogger(UI.class);
    public void Entry() {
        Head();
        MainPrompt();
    }

    public void Head() {
        logger.info("------------------------------------\n" +
                "|          饿了吗后台管理系统          |\n" +
                "------------------------------------");
    }

    public void MainPrompt() {
        logger.info("====1.管理员登录=2.商家登录=3.顾客点单=4.退出系统===\n" +
                "请输入你的选择:");
    }

    public void AdminPrompt() {
        logger.info("=========1.所有商家列表=2.搜索商家=3.新建商家=4.删除商家=5.退出系统=========\n"
                + "请输入你的选择:");
    }

    public void MerchantHead() {
        logger.info("商家编号  商家名称  商家地址  商家介绍  起送费  配送费");
    }

    public void MerchantPrompt() {
        logger.info("======= 一级菜单 (商家管理) 1.查看商家信息=2.修改商家信息=3.更改密码=4.所属商品管理=5.退出系统=======\n"
                + "请输入你的选择:");
    }

    public void FoodPrompt() {
        logger.info("======= 二级菜单 (食品管理) 1.查看食品列表=2.新增食品=3.修改食品=4.删除食品=5.返回一级菜单 =======\n"
                + "请输入你的选择:");
    }

    public void FoodHead() {
        logger.info("食品编号  食品名称  食品介绍  食品价格");
    }

    public void error() { logger.info("服务器连接失败!");}

}