package org.logbug.extract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map; 



public class SimilarDegreeByCos {	
	
	public HashSet<String> getUnionVector(Map<String, Double>list1, Map<String, Double> list2) {
		HashSet <String> unionSets = new HashSet<String>();		
		
		for(String word: list1.keySet()) {
			unionSets.add(word);
		}
		for(String word: list2.keySet()) {
			unionSets.add(word);
		}
				
		return unionSets;
	}
	
	public Map<String, Double> addSurplus(HashSet<String> unionSets, Map<String,Double> list) {
		double fillEelem = 0.00;
		Map<String, Double> surplus = new HashMap<String, Double>();
		
		for(String word : unionSets) {
			
			if(!list.containsKey(word))
				surplus.put(word, fillEelem);
			else
				surplus.put(word, list.get(word));
		}		
		return surplus;		
	}
	public double computeSimilaryDegree(Map<String, Double>map1, Map<String, Double> map2) {
		
		/** 若list1与list2的向量空间不等，则补足list1与list2的向量空间 */
		if (map1.size()!=map2.size()) {
			HashSet<String> unionSets = getUnionVector(map1, map2);
			Map<String, Double> surplusMap1 = addSurplus(unionSets, map1);
			Map<String, Double> surplusMap2 = addSurplus(unionSets, map2);
			map1 = surplusMap1;
			map2 = surplusMap2;
		}		
			
		/**相似度计算**/
		double vector1Modulo = 0.00;//向量1的摸
		double vector2Modulo = 0.00;//向量2的摸
		double vectorProduct = 0.00;//向量积
		List<Double> list1 = new ArrayList<Double>();
		List<Double> list2 = new ArrayList<Double>();
		/**list1的size应与list2的size相等**/
		for(String word : map1.keySet()) {
			double vector1 = map1.get(word);
			vector1Modulo += vector1*vector1;
			list1.add(vector1);
		}
		for(String word : map2.keySet()) {
			double vector2 = map2.get(word);
			vector2Modulo += vector2 * vector2;
			list2.add(vector2);
		}
		for(int i=0; i<list1.size();i++) {
			double vector1 = list1.get(i);
			double vector2 = list2.get(i);			
			vectorProduct += vector2*vector1;
		}
		vector1Modulo = Math.sqrt(vector1Modulo);
		vector2Modulo = Math.sqrt(vector2Modulo);	
		vectorProduct = vectorProduct/(vector1Modulo*vector2Modulo);
		return vectorProduct;
	}
	public static double  getSimilaryDegree(Map<String, Double> list1, Map<String, Double> list2) 
	{

		SimilarDegreeByCos sim = new SimilarDegreeByCos();

		return sim.computeSimilaryDegree(list1, list2);
	}
	
	public static void main(String args[]) 
	{
		Map<String, Double> list1 = new HashMap<String, Double>();
		Map<String, Double> list2 = new HashMap<String, Double>();
		list1.put("aaa", 0.23);
		list1.put("bbb", 0.45);
		list1.put("ccc", 0.78);
		
		list2.put("ggg", 0.75);
		list2.put("fff", 0.75);
		list2.put("aaa", 0.12);
		list2.put("ttt", 0.45);
		list2.put("ccc", 0.45);
		SimilarDegreeByCos sim = new SimilarDegreeByCos();
//		TreeMap setsStrtext = sim.getUnionVector(list1, list2);
		
		System.out.println(sim.getSimilaryDegree(list1, list2));
		System.out.println("\n");
//	    Iterator it=setsStrtext.iterator();
//
//       while(it.hasNext())
//       {
//    	   System.out.print(it.next() +" ");
//       }
	}
}
