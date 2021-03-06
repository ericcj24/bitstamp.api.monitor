import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.math.plot.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionAPI{
	//{"date": "1395551812", "tid": 4151239, "price": "564.23", "amount": "0.05000000"}
	
	static class Transactions {
	    private String date;
	    private int tid;
	    private String price;
	    private String amount;
	    public Transactions() {
	    }
	    public Transactions(String date, int tid, String price, String amount) {
	      this.date = date;
	      this.tid = tid;
	      this.price = price;
	      this.amount = amount;
	    }
	    @Override
	    public String toString() {
	      return String.format("(date=%s, tid=%d, price=%s, amount=%s)", date, tid, price, amount);
	    }
	    
	    public String getDate(){
	    	return date;
	    }
	    public int getTid(){
	    	return tid;
	    }
	    public String getPrice(){
	    	return price;
	    }
	    public String getAmount(){
	    	return amount;
	    }
	}
	
    public static Vector<Vector<Double>> HttpGetTransactions() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet("https://www.bitstamp.net/api/transactions/");
            CloseableHttpResponse response1 = httpclient.execute(httpGet);

            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                System.out.println(entity1.getContentType()+" Transaction API");
               
                ObjectMapper mapper = new ObjectMapper();
                ArrayList<Transactions> array = mapper.readValue(entity1.getContent(), new TypeReference<ArrayList<Transactions>>() { });

                /*for(int i=0; i<array.size(); i++){
                	long dateLong = Long.parseLong(array.get(i).date)*1000;
                	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                	String formattedDate =  sdf.format(dateLong);
                	System.out.println("date: "+formattedDate+ " tid " + array.get(i).tid + " price "+array.get(i).price+" amount "+array.get(i).amount);
                }*/
                
                
                Vector<Double> x = new Vector<Double>();
                Vector<Double> y = new Vector<Double>();
                for(int i=0; i<array.size(); i++){
                	x.add(Double.parseDouble(array.get(i).date));
                	y.add(Double.parseDouble(array.get(i).price));
                }
                
                Vector<Vector<Double>> v = new Vector<Vector<Double>>();
                v.add(x);
                v.add(y);
                
                EntityUtils.consume(entity1);
                
                return v;
                
            } finally {
                response1.close();
            }

        } finally {
            httpclient.close();

        }
    }

}