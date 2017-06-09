package exampleInstance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes; 
import org.xml.sax.SAXException; 
import org.xml.sax.XMLReader; 
import org.xml.sax.helpers.DefaultHandler; 
import org.xml.sax.helpers.XMLReaderFactory; 
public class SAXParser {
	
	class BookHandler extends DefaultHandler { 
	      private List<String> nameList;
	      private List<String> authorList;
	      private List<String> bookIdList;
	      
	      private boolean title = false; 
	      private boolean author = false;
	      private boolean bookid = false;
	   
	      public List<String> getNameList() { 
	         return nameList; 
	      } 
	      public List<String> getAuthorList() {
	    	  return authorList;
	      }
	      public List<String> getBookIdList() {
	    	  return bookIdList;
	      }
	      
	      // Called at start of an XML document 
	      @Override 
	      public void startDocument() throws SAXException { 
	         System.out.println("Start parsing document..."); 
	         nameList = new ArrayList<String>();
	         authorList = new ArrayList<String>();
	         bookIdList = new ArrayList<String>();
	      } 
	      // Called at end of an XML document 
	      @Override 
	      public void endDocument() throws SAXException {  
	         System.out.println("End");  
	      } 
	      
	      /** 
	       * Start processing of an element. 
	       * @param uri：namespaceURI  Namespace URI 
	       * @param localName：是包括命名空间的的标签，如果没有命名空间则为空
	       * @param qName:	  是不包含命名空间的标签   
	       * @param atts     集合属性 
	       */ 
	      @Override 
	      public void startElement(String uri, String localName, String qName, 
		     Attributes atts) throws SAXException { 
	         // Using qualified name because we are not using xmlns prefixes here. 
	         if (qName.equals("title")) { 
	            title = true; 
	         }
	         if (qName.equals("author")) {
	        	 author = true;
	         }
	         //if (qNam.equals(""))
	         if (atts != null) {
	        	 for (int i = 0; i<atts.getLength(); i++) {
	        		 // getQName()是获取属性名称
	        		 System.out.println(atts.getQName(i)+atts.getValue(i));
	        	 }
	        		 
	         }
	      } 
	   
	      @Override 
	      public void endElement(String namespaceURI, String localName, String qName) 
	         throws SAXException { 
	         // End of processing current element 
	         if (title) { 
	            title = false; 
	         } 
	         if (author) {
	        	 author = false;
	         }
	      } 
	   			
	      @Override 
	      public void characters(char[] ch, int start, int length) { 
	         // Processing character data inside an element 
	         if (title) { 
	            String bookTitle = new String(ch, start, length); 
	            System.out.println("Book title: " + bookTitle); 
	            nameList.add(bookTitle); 
	         } 
	         if (author) { 
		            String bookTitle = new String(ch, start, length); 
		            System.out.println("Book author: " + bookTitle); 
		            authorList.add(bookTitle); 
		         } 
	      } 
				
}
	public static void main(String[] args) throws SAXException, IOException { 
		
	      XMLReader parser = XMLReaderFactory.createXMLReader(); 
	      BookHandler bookHandler = (new SAXParser()).new BookHandler(); 
	      parser.setContentHandler(bookHandler); 
	      parser.parse("e:/books.xml"); 
	      System.out.println(bookHandler.getNameList()); 
	   
	} 
}
