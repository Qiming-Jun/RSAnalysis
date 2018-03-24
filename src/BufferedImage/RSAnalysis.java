package BufferedImage;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class RSAnalysis {
	
	String path;
	double Rm, Sm, R_m, S_m;
	
	public RSAnalysis(String path) {
		this.path = path;
		this.Rm = 0;
		this.Sm = 0;
		this.R_m = 0;
		this.S_m = 0;
	}
	
	public void analyse() {
		try{
			BufferedImage bimg = ImageIO.read(new File(this.path));
			double Rm = 0;
			double Sm = 0;
			double R_m = 0;
			double S_m = 0;
            int width = bimg.getWidth();
            int height = bimg.getHeight();
            int[][] R = new int[width][height];
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++)
                	R[i][j] = (bimg.getRGB(i,j)&0xff0000)>>16; //�õ�ͼƬ��Rͨ��
            int r = 0;
            int c = 0;
            for(int i=1; i<width/4; i++) {
            	c = 0;
            	for(int j=1; j<height/4; j++) {
            		int[][] temp = new int[4][4];
            		for(int m=4*r; m<=4*r+3; m++)
            			for(int n=4*c; n<=4*c+3; n++){
            				temp[m-4*r][n-4*c] = R[m][n];
            			}
            		int x = nonnegativeFlip(temp);
    				int y = nonpositiveFlip(temp);
    				if(x == 1)
    					Rm++;
    				else if(x == -1)
    					Sm++;
    				if(y == 1)
    					R_m++;
    				else if(y == -1)
    					S_m++;
            		c++;
            	}
            	r++;
            }
            this.Rm = Rm/(width*height/16.0);
            this.Sm = Sm/(width*height/16.0);
            this.R_m = R_m/(width*height/16.0);
            this.S_m = S_m/(width*height/16.0);
            System.out.println(path);
            System.out.printf("Rm��%f\tR-m��%f\nSm��%f\tS-m��%f\n", this.Rm, this.R_m, this.Sm, this.S_m);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static float calculate(int[][] square) { //����4*4Сͼ�������������
		float f = 0;
		f += Math.abs(square[0][0]-square[1][0]);
		f += Math.abs(square[1][0]-square[0][1]);
		f += Math.abs(square[0][1]-square[0][2]);
		f += Math.abs(square[0][2]-square[1][1]);
		f += Math.abs(square[1][1]-square[2][0]);
		f += Math.abs(square[2][0]-square[3][0]);
		f += Math.abs(square[3][0]-square[2][1]);
		f += Math.abs(square[2][1]-square[1][2]);
		f += Math.abs(square[1][2]-square[0][3]);
		f += Math.abs(square[0][3]-square[1][3]);
		f += Math.abs(square[1][3]-square[2][2]);
		f += Math.abs(square[2][2]-square[3][1]);
		f += Math.abs(square[3][1]-square[3][2]);
		f += Math.abs(square[3][2]-square[2][3]);
		f += Math.abs(square[2][3]-square[3][3]);
		f /= 15.0;
		return f;
	}
	
	public int nonnegativeFlip(int[][] square) { //�Ǹ���ת
		float a = calculate(square);
		for(int i=0; i<=3; i++)
			for(int j=0; j<=3; j++) {
				int flag = (int)(Math.random()*2);
				if(flag >= 1)
					square[i][j] = square[i][j]&0xfe; //�Ǹ���ת
			}
		float b = calculate(square);
		if(a < b)
			return 1; //�������������
		else if(a > b)
			return -1; //��������Լ�С
		else 
			return 0; //��������Բ���
	}
	
	public  int nonpositiveFlip(int[][] square) { //������ת
		float a = calculate(square);
		for(int i=0; i<=3; i++)
			for(int j=0; j<=3; j++) {
				int flag = (int)(Math.random()*2);
				if(flag >= 1) {
					if((square[i][j]&0x1) == 0) //������ת
						square[i][j] -= 1;
					else
						square[i][j] += 1;
				}
			}
		float b = calculate(square);
		if(a < b)
			return 1; //�������������
		else if(a > b)
			return -1; //��������Լ�С
		else
			return 0; //��������Բ���
	}
	
}
