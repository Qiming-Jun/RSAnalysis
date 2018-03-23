package BufferedImage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BufferedImageTest {
		
	public static void main(String [] args) {
		String path = "G:\\rose.jpg";
		String secretPath = "G:\\me.jpg";
		String newPath1 = "G:\\informationHiding.jpg";
		String newPath2 = "G:\\secretPhoto.jpg";
		int[][] R = LBSofR(path, secretPath);
		BufferedImage bimg = updatePic(R, 'R', path);
		createNewPic(bimg, newPath1);
		getSecret(57086, 'R', newPath1, newPath2); //57086Ϊ������Ϣ���ֽ���
    }
	
	public static void getSecret(int size, int channelCode, String path, String newPath) {
		try {
			BufferedImage bimg = ImageIO.read(new File(path));
			int width = bimg.getWidth();
            int height = bimg.getHeight();
            byte[] secretData = new byte[size];
            int count = 1;
            int index = 0;
            int temp = 0;
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++) {
                	int channel = 0;
                	switch(channelCode) {
                		case 'R':
                			channel = (bimg.getRGB(i,j)&0xff0000)>>16; //��Rͨ����ȡ
                			break;
                		case 'G':
                			channel = (bimg.getRGB(i,j)&0xff00)>>8; //��Gͨ����ȡ
                			break;
                		case 'B':
                			channel = bimg.getRGB(i,j)&0xff; //��Bͨ����ȡ
                			break;
                		default:
                			break;
                	}
                    temp += (channel&0x1)*Math.pow(2, (count-1)%8); //�ӵͱ���λ���߱���λ����
                    if((count++)%8 == 0) { //��ȡ��һ�ֽ�����
                    	secretData[index++] = (byte)temp;
                    	temp = 0;
                    }
                    if(index == size) {
                    	try {
                    		FileOutputStream fs = new FileOutputStream(newPath);
                    		fs.write(secretData); //����������ϢͼƬ
                    		fs.close();
                    		return;
                    	} catch (Exception e) {
                    		e.printStackTrace();
                    		return;
                    	}
                    }
                }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static BufferedImage updatePic(int[][] newChannel, char channelCode, String path) {
		try {
			BufferedImage bimg = ImageIO.read(new File(path));
			int width = bimg.getWidth();
            int height = bimg.getHeight();
			switch(channelCode) {
				case 'R':
					for(int i=0; i<height; i++)
		                for(int j=0; j<width; j++) {
		                    int rgb = bimg.getRGB(i,j);
		                    int g = rgb&0xff00;
		                    int b = rgb&0xff;
		                    rgb = newChannel[i][j]*256*256 + g + b + 0xff000000; //��Rͨ������
		                    bimg.setRGB(i, j, rgb);
		                }
					break;
				case 'G':
					for(int i=0; i<height; i++)
		                for(int j=0; j<width; j++) {
		                    int rgb = bimg.getRGB(i,j);
		                    int r = rgb&0xff0000;
		                    int b = rgb&0xff;
		                    rgb = r + newChannel[i][j]*256 + b + 0xff000000; //��Gͨ������
		                    bimg.setRGB(i, j, rgb);
		                }
					break;
				case 'B':
					for(int i=0; i<height; i++)
		                for(int j=0; j<width; j++) {
		                    int rgb = bimg.getRGB(i,j);
		                    rgb = (rgb&0xffffff00) + newChannel[i][j]; //��Bͨ������
		                	bimg.setRGB(i, j, rgb);
		                }
					break;
				default:
					break;
			}
			return bimg;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void createNewPic(BufferedImage bimg, String newPath) {
		try {
			File file = new File(newPath);
			ImageIO.write(bimg, "bmp", file); //������д���ͼƬ
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int[][] LBSofR(String path, String secretPath) {
		int[][] R = getRData(path); //ԭͼƬ��Rͨ��
        byte[] secretData = getByteFlow(secretPath); //����ͼƬ���ֽ���
        int count = 0;//��¼��ȡ���ı���λ��
        for(int i=0; i<secretData.length; i++){
        	int a = secretData[i]&0xff;
        	for(int j=0; j<8; j++) {
        		int LB = a&0x1; //���λ�ȡ�ֽڵ�ÿһλ
        		int w = count/R.length;
        		int h = count%R.length;
        		R[w][h] = (R[w][h]&0xfe) + LB; //���λ����Ϊ������Ϣ������
        		count++;
        		a = a>>1;
        	}
        }
        return R;
	}
	
	public static int[][] LBSofG(String path, String secretPath) {
		int[][] G = getGData(path); //ԭͼƬ��Gͨ��
        byte[] secretData = getByteFlow(secretPath); //����ͼƬ���ֽ���
        int count = 0; //��¼��ȡ���ı���λ��
        for(int i=0; i<secretData.length; i++){
        	int a = secretData[i]&0xff;
        	for(int j=0; j<8; j++) {
        		int LB = a&0x1; //���λ�ȡ�ֽڵ�ÿһλ
        		int w = count/G.length;
        		int h = count%G.length;
        		G[w][h] = (G[w][h]&0xfe) + LB; //���λ����Ϊ������Ϣ������
        		count++;
        		a = a>>1;
        	}
        }
        return G;
	}
	
	public static int[][] LBSofB(String path, String secretPath) {
		int[][] B = getBData(path); //ԭͼƬ��Bͨ��
        byte[] secretData = getByteFlow(secretPath); //����ͼƬ���ֽ���
        int count = 0; //��¼��ȡ���ı���λ��
        for(int i=0; i<secretData.length; i++){
        	int a = secretData[i]&0xff;
        	for(int j=0; j<8; j++) {
        		int LB = a&0x1; //���λ�ȡ�ֽڵ�ÿһλ
        		int w = count/B.length;
        		int h = count%B.length;
        		B[w][h] = (B[w][h]&0xfe) + LB; //���λ����Ϊ������Ϣ������
        		count++;
        		a = a>>1;
        	}
        }
        return B;
	}
	
	public static byte[] getByteFlow(String path) {
		try {
            BufferedImage bimg = ImageIO.read(new File(path));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(bimg, "jpg", out);
            byte[] data = out.toByteArray(); //���ͼƬ���ֽ���
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public static int[][] getData(String path) {
        try {
            BufferedImage bimg = ImageIO.read(new File(path));
            int width = bimg.getWidth();
            int height = bimg.getHeight();
            int[][] data = new int[width][height];
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++)
                    data[i][j] = bimg.getRGB(i,j); //�õ�ͼƬ��λͼ����
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static int[][] getRData(String path) {
        try {
            BufferedImage bimg = ImageIO.read(new File(path));
            int width = bimg.getWidth();
            int height = bimg.getHeight();
            int[][] data = new int[width][height];
            int[][] R = new int[width][height];
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++) {
                    data[i][j] = bimg.getRGB(i,j); //�õ�ͼƬ��λͼ����
                    R[i][j] = (data[i][j]&0xff0000)>>16; //�õ�Rͨ������
                }
            return R;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static int[][] getGData(String path) {
        try {
            BufferedImage bimg = ImageIO.read(new File(path));
            int width = bimg.getWidth();
            int height = bimg.getHeight();
            int[][] data = new int[width][height];
            int[][] G = new int[width][height];
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++) {
                    data[i][j] = bimg.getRGB(i,j); //�õ�ͼƬ��λͼ����
                    G[i][j] = (data[i][j]&0xff00)>>8; //�õ�Gͨ������
                }
            return G;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static int[][] getBData(String path) {
        try {
            BufferedImage bimg = ImageIO.read(new File(path));
            int width = bimg.getWidth();
            int height = bimg.getHeight();
            int[][] data = new int[width][height];
            int[][] B = new int[width][height];
            for(int i=0; i<height; i++)
                for(int j=0; j<width; j++) {
                    data[i][j] = bimg.getRGB(i,j); //�õ�ͼƬ��λͼ����
                    B[i][j] = data[i][j]&0xff; //�õ�Bͨ������
                }
            return B;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
}
