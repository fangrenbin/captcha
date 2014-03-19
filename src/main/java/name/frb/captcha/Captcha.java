/*
 * Create Author  : renbin.fang
 * Create Date    : 2013-7-26
 * File Name      : CaptchaUtil.java
 */
package name.frb.captcha;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * 验证码工具
 * <p/>
 *
 * @author : renbin.fang
 * @date : 2013-7-26
 */
public class Captcha {
    /**
     * 验证码内容区间
     */
    private static String range = "0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * 验证码位数
     */
    private static final int SIZE = 4;

    /**
     * 干扰线条数
     */
    private static final int LINES = 20;

    /**
     * 图片宽度
     */
    private static final int WIDTH = 230;

    /**
     * 图片高度
     */
    private static final int HEIGHT = 100;

    /**
     * 字体大小
     */
    private static final int FONT_SIZE = 90;

    public static void main(String[] args) {
        Map<String, InputStream> captchaMap = getCaptcha();
        for (String s : captchaMap.keySet()) {
            System.out.println(s);
        }
    }

    /**
     * 生成验证码Map<br />
     * Map<String, InputStream>:<br />
     * Key:String值为验证码内容 <br />
     * Value:InputStream值为验证码图片InputStream
     * <p/>
     * Author renbin.fang, 2013-7-11
     *
     * @return 返回验证码Map
     */
    public static Map<String, InputStream> getCaptcha() {
        StringBuffer captcharTextBuffer = new StringBuffer();
        BufferedImage captchaBufferdImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 获取画笔
        Graphics graphic = captchaBufferdImage.getGraphics();
        graphic.setColor(Color.LIGHT_GRAY);
        graphic.fillRect(0, 0, WIDTH, HEIGHT);

        // 画随机字符
        for (int i = 1; i <= SIZE; i++) {
            char toWriteText = getRandomString();
            graphic.setColor(getRandomColor());
            graphic.setFont(new Font(null, Font.BOLD, FONT_SIZE));
            graphic.drawString(String.valueOf(toWriteText), (i - 1) * WIDTH / 4, HEIGHT - 20);
            captcharTextBuffer.append(toWriteText);
        }

        // 画干扰线
        for (int i = 1; i <= LINES; i++) {
            graphic.drawLine(getCoordinate(WIDTH), getCoordinate(HEIGHT), getCoordinate(WIDTH), getCoordinate(HEIGHT));
        }

        // 将BufferedImage 转为 InputStream，用于action result返回
        Map<String, InputStream> captchaMap = new HashMap<String, InputStream>();
        InputStream inputStream = null;
        ByteArrayOutputStream baOutput = new ByteArrayOutputStream();
        ImageOutputStream imageOutputStream = null;
        try {
            imageOutputStream = ImageIO.createImageOutputStream(baOutput);
            ImageIO.write(captchaBufferdImage, "jpg", imageOutputStream);
            inputStream = new ByteArrayInputStream(baOutput.toByteArray());

            captchaMap.put(captcharTextBuffer.toString(), inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baOutput.close();
                imageOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return captchaMap;
    }

    /**
     * 随机生成干扰线的坐标
     * <p/>
     * Author renbin.fang, 2013-7-11
     *
     * @param range 干扰线坐标范围
     * @return 坐标的点
     */
    private static int getCoordinate(int range) {
        return new Random().nextInt(range);
    }

    /**
     * 随机返回在字符串中的任一字符
     * <p/>
     * Author renbin.fang, 2013-7-11
     *
     * @return 字符串range中的任一字符
     */
    private static char getRandomString() {
        return range.charAt(new Random().nextInt(range.length()));
    }

    /**
     * 返回随机颜色
     * <p/>
     * Author renbin.fang, 2013-7-11
     *
     * @return 随机颜色
     */
    private static Color getRandomColor() {
        Random randomRGB = new Random();
        int colorR = randomRGB.nextInt(180);
        int colorG = randomRGB.nextInt(180);
        int colorB = randomRGB.nextInt(180);

        return new Color(colorR, colorG, colorB);
    }
}