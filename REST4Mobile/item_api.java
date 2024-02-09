package rest.demo;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;

import java.sql.*;

//import rest.demo.ItemEntry;

@Path("/product")
public class item_api {
	
	private PreparedStatement query = null;
    DBconnect connect=new DBconnect();
    
	@GET
	@Path("/001")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnItemsFromLoc1() throws Exception{
		String returnString=null;
		Response rb = null;
        Connection conn= connect.open();
        ResultSet rs;
        try {   
        	query = conn.prepareStatement("SELECT * " +
        									"FROM rest_demo.item WHERE loc='1'");
            rs = query.executeQuery();
            
            toJSON converter = new toJSON();
            JSONArray	json = new JSONArray();
            
            json	=	converter.convert(rs);
            query.close();

            returnString = json.toString();
            rb = Response.ok(returnString).build();
            
        } catch (Exception e) {
                throw e;
        } finally {
                connect.close();
        }

		return rb;
    }
    
	@GET
	@Path("/002")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnItemsFromLoc2() throws Exception{
		String returnString=null;
		Response rb = null;
        Connection conn= connect.open();
        ResultSet rs;
        
        try {   
        	query = conn.prepareStatement("SELECT * " +
        									"FROM rest_demo.item WHERE loc='2'");
            rs = query.executeQuery();
            
            toJSON converter = new toJSON();
            JSONArray	json = new JSONArray();
            
            json	=	converter.convert(rs);
            query.close();

            returnString = json.toString();
            rb = Response.ok(returnString).build();
            
        } catch (Exception e) {
                throw e;
        } finally {
                connect.close();
        }

		return rb;
	}
	
	@POST
	@Path("/bid")
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response bidConsent(String data) throws Exception{
		Response rb = null;
        Connection conn= connect.open();
		try{
			System.out.println("Incomming data:"+data);
			ObjectMapper mapper = new ObjectMapper();
			ItemEntry itemEntry = mapper.readValue(data, ItemEntry.class);
			
			query = conn.prepareStatement("INSERT INTO bid " +
					"(item_code, supplier_id, amount_quoted) " +
					"VALUES (?, ?, ?) ");
			
			query.setInt(1, itemEntry.item_code);
			query.setInt(2, itemEntry.supplier_id);
			query.setFloat(3, itemEntry.amount_quoted);			
			
			query.executeUpdate();
            query.close();

            rb = Response.ok("Success!").build();
            
        } catch (Exception e) {
                throw e;
        } finally {
                connect.close();
        }

		return rb;
	}
	
	@GET
	@Path("/")	
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnItems(
			@QueryParam("code") int code) throws Exception{
		String returnString=null;
		Response rb = null;
        Connection conn= connect.open();
        ResultSet rs;
        
        try {   
        	query = conn.prepareStatement("SELECT * " +
        									"FROM rest_demo.item WHERE code="+code);
            rs = query.executeQuery();
            
            toJSON converter = new toJSON();
            JSONArray	json = new JSONArray();
            
            json	=	converter.convert(rs);
            query.close();

            returnString = json.toString();
            rb = Response.ok(returnString).build();
            
        } catch (Exception e) {
                throw e;
        } finally {
                connect.close();
        }

		return rb;
	}
}