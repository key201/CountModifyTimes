package utils;

import java.io.IOException;

import jxl.write.WriteException;

public class Levenshtein {
	
	/**
	 * @param s1
	 * @param s2
	 * @return Levenshtein Distance
	 */	
	public static double getStringDistance(String s1, String s2){
		
		double distance[][];//定义距离表
		int s1_len = s1.length();
		int s2_len = s2.length();
		
		if (s1_len==0) {
			return s2_len;
		}
		if (s2_len==0) {
			return s2_len;
		}
		
		distance = new double[s1_len+1][s2_len+1];
		
		// 二维数组第一行和第一列放置自然数
		for(int i=0; i<=s1_len;i++) {
			distance[i][0] = i;
		}
		for(int i=0; i<=s2_len;i++) {
			distance[0][i]=i;
		}
		
		//比较，若行列相同，则代价为0，否则代价为1
		for(int i=1; i<=s1_len; i++) {
			char s1_i = s1.charAt(i-1);
			for(int j=1; j<=s2_len; j++) {
				char s2_j = s2.charAt(j-1);
				if(s1_i==s2_j) {
					distance[i][j] =distance[i-1][j-1];
				}else {
					// 否则代价取1，取左上角、左、上最小值+代价（代价之和便是最终距离）
					distance[i][j] = getMin(distance[i-1][j], distance[i][j-1], distance[i-1][j-1])+1;
				}
			}
			
		}
		// 取二维数组最后一位便是两个字符串之间的距离
		return distance[s1_len][s2_len];
		
	}

	private static double getMin(double a, double b, double c) {
		
		double min =a;
		if(b <min) {
			min = b;
		}
		if(c<min){
			min = c;
		}
		return min;
	}
	
	public static void main(String[] args) {
		String s1="ABCDEFGZ";
		String s2="ABFDFEGXY";
		
		double distance1 = getStringDistance(s1, s2);
		System.out.println(distance1);
		
	}

}
