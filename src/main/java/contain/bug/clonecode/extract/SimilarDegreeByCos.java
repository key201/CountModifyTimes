package contain.bug.clonecode.extract;
import java.util.HashMap;  
import java.util.Iterator;  
import java.util.Map; 
public class SimilarDegreeByCos {

	public static double getSimilaryDegree(String str1, String str2) {

		//创建向量空间模型,使用map实现，主键
		//为此项，职位长度为2的数组，存放着杜英词项
		//在字符串中的出现次数

		Map<String,int[]>vectorSpace = new HashMap<String, int[]>();
		int [] itemCountArray = null;//为了避免

		//以空格为分割符，分解字符串
		String strArray[] = str1.split(" ");
		for(int i=0; i<strArray.length; ++i)
		{
			if(vectorSpace.containsKey(strArray[i])) {
			
				++(vectorSpace.get(strArray[i])[0]);
			} 
			else 
			{
				itemCountArray = new int[2];
				itemCountArray[0] = 1;
				itemCountArray[1] = 0;
				vectorSpace.put(strArray[i], itemCountArray);
			}			
		}
		strArray = str2.split(" ");
		for(int i=0; i<strArray.length; ++i)
		{
			if(vectorSpace.containsKey(strArray[i]))
				++(vectorSpace.get(strArray[i])[1]);
			else {
				itemCountArray = new int [2];
				itemCountArray[0] = 0;
				itemCountArray[1] = 1;
				vectorSpace.put(strArray[i], itemCountArray);
			}
		}
		//计算相似度
		double vector1Modulo = 0.00;//向量1的摸
		double vector2Modulo = 0.00;//向量2的摸
		double vectorProduct = 0.00;//向量积
		Iterator iter = vectorSpace.entrySet().iterator();		
		
		while (iter.hasNext())
		{
			Map.Entry<String, int []> entry = (Map.Entry)iter.next();
			itemCountArray = (int[])entry.getValue();
			
			vector1Modulo += itemCountArray[0]*itemCountArray[0];
			vector2Modulo += itemCountArray[1]*itemCountArray[1];
			
			vectorProduct += itemCountArray[0]*itemCountArray[1];
		}
		vector1Modulo = Math.sqrt(vector1Modulo);
		vector2Modulo = Math.sqrt(vector2Modulo);
		
		//返回相似度
		return (vectorProduct/(vector1Modulo*vector2Modulo));		
	}
	
	
	public static void main(String args[]) 
	{
		String str1 = "gold silver truck";
		String str2 = "Shipment of gold damaged in a fire";
		String str3 = "Delivery of silver arrived in a silver truck";
		String str4 = "Shipment of gold arrived in a truck";
		String str5 = "gold gold gold gold gold gold";
		
		System.out.println(SimilarDegreeByCos.getSimilaryDegree(str1, str2));
		System.out.println(SimilarDegreeByCos.getSimilaryDegree(str1, str3));
		System.out.println(SimilarDegreeByCos.getSimilaryDegree(str1, str4));
		System.out.println(SimilarDegreeByCos.getSimilaryDegree(str1, str5));
	}
}
