package com.tz.book.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ValidateCodeUtil {
   
	// ͼƬ�Ŀ�ȡ�
    private int width = 100;
    // ͼƬ�ĸ߶ȡ�
    private int height = 30;
    // ��֤���ַ�����
    private int codeCount = 4;
    // ��֤���������
    private int lineCount = 10;
    // ��֤��
    private String code = null;
    // ��֤��ͼƬBuffer
    private BufferedImage buffImg = null;
    //���������
    private Random random = new Random();

    public ValidateCodeUtil() {
        creatImage();
    }

    public ValidateCodeUtil(int width, int height) {
        this.width = width;
        this.height = height;
        creatImage();
    }

    public ValidateCodeUtil(int width, int height, int codeCount) {
    	this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        creatImage();
    }

    public ValidateCodeUtil(int width, int height, int codeCount, int lineCount) {
    	this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        creatImage();
    }
    
    // �õ�����ַ�
    private String randomStr(int n) {
        String str1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        String str2 = "";//���ɵ�����ַ���ɵ��ַ���
        int len = str1.length();
        double r;//�����������
        for (int i = 0; i < n; i++) {
            r = (Math.random()) * len;
            str2 = str2 + str1.charAt((int) r);
        }
        return str2;
    }

    // �õ������ɫ
    private Color getRandColor() {

        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }
    
    /**
     * �����������
     */
    private Font getFont(int size) {
        Random random = new Random();
        Font font[] = new Font[5];
        font[0] = new Font("Ravie", Font.PLAIN, size);
        font[1] = new Font("Antique Olive Compact", Font.PLAIN, size);
        font[2] = new Font("Fixedsys", Font.PLAIN, size);
        font[3] = new Font("Wide Latin", Font.PLAIN, size);
        font[4] = new Font("Gill Sans Ultra Bold", Font.PLAIN, size);
        return font[random.nextInt(5)];
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {
        return code.toLowerCase();
    }
    
 
    // ����ͼƬ
    private void creatImage() {
        int fontWidth = width / codeCount;// ����Ŀ��
        int fontHeight = height-5;// ����ĸ߶�

        // ͼ��buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //Graphics g = buffImg.getGraphics();
        Graphics2D g = buffImg.createGraphics();
        // ���ñ���ɫ
        g.setColor(getRandColor());
        g.fillRect(0, 0, width, height);
               
        // ����������ʽ
        Font font = getFont(fontHeight);
        g.setFont(font);

        // ���ø�����
        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            g.setColor(getRandColor());
            g.drawLine(xs, ys, xe, ye);
        }

        // ������
        float yawpRate = 0.01f;// ������
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            buffImg.setRGB(x, y, random.nextInt(256));
        }

        String str1 = randomStr(codeCount);// �õ�����ַ�
        this.code = str1;
        for (int i = 0; i < codeCount; i++) {
            String strRand = str1.substring(i, i + 1);
            g.setColor(getRandColor());
            
            //����������ת
            int degree=random.nextInt()%30;
            g.rotate(degree*Math.PI/180,i*fontWidth+3,height-4);
            // g.drawString(a,x,y);
            // aΪҪ�������Ķ�����x��y��ʾҪ���Ķ���������ַ��Ļ���λ�ڴ�ͼ������������ϵ�� (x, y) λ�ô�
            g.drawString(strRand, i*fontWidth+3, height-4);
            g.rotate(-degree*Math.PI/180,i*fontWidth+3,height-4);
        }
        
    }
    
    //ʹ�÷���
    public static void createValidateCode(HttpServletResponse response,HttpSession session) throws IOException{
        // ������Ӧ�����͸�ʽΪͼƬ��ʽ
        response.setContentType("image/jpeg");
        //��ֹͼ�񻺴档
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        
        
        ValidateCodeUtil vCode = new ValidateCodeUtil(80,30,4,5);
        session.setAttribute("validateCode", vCode.getCode());
        vCode.write(response.getOutputStream());
     }

}