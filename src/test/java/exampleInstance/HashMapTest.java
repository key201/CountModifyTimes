package exampleInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HashMapTest {


	public static void main(String[] args){
		Map<String, String> map = new TreeMap<String, String>();
		map.put("c", "cccccc");
		map.put("a", "aaaaaa");
		map.put("b", "bbbbbb");
		map.put("d", "dddddd");
		map.put("g", "gggggggg");
		map.put("f", "ffffff");
		List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String,String>>(map.entrySet());
	
		Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
			//升序
			public int compare(Entry<String, String>o1, Entry<String, String>o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		}
				);
		
		for(Map.Entry<String, String>mapping : list) {
			System.out.println(mapping.getKey() + ":" + mapping.getValue());
		}
		
	}
}