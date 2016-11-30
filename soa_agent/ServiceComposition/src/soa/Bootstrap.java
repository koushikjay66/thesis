/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soa;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import json.Builder.Builder;
import json.Builder.objects.M2M_Request;
import json.Builder.objects.M2M_Response;
import leech.Leech;

/**
 *
 * @author Koushik
 */
public class Bootstrap {

    private final M2M_Request req;


    public Bootstrap(M2M_Request req) {
        this.req = req;
    }

    // This compile method is the driver for the Bootstrap class . 
    // It handles all the things. 
    // Including the main SOA class and the leech class.
    public M2M_Response compile() throws NoSuchAlgorithmException {
        // If it is REQ then I know The request has come and M2M Device 
        // In other words it is a normal REQUEST.
        // Now I need to call the SOA class
        if (req.TYPE.equalsIgnoreCase("REQ")) {
            return callSOA();
        }

        return null;
    }

    private M2M_Response callSOA() {
        SOA soa = new SOA(req.SERVICE_NAME);

        try {

            M2M_Response mr = soa.search();
            // The below for loop means I have given some optional parameter.
            if(mr ==null){
                System.out.println("Kiccu Khuija Painai Need to req to agent ");
            }
            
            else if (!req.COMPONENTS.isEmpty() && mr!=null  && !mr.B_Service.isEmpty() && (req.COMPONENTS.size()!=mr.B_Service.size())) {
                
                // I have got some service but have not got all of them . 
                // It is time to start leeching.
                System.out.println("going to leech aroung");
                Leech ll = new Leech(req, mr);
                 ll.partial_service();
                 return ll.result();
                 
            }else{
                mr.Token=req.TOKEN;
                return mr;
            }
        } catch (Exception ex) {
            Logger.getLogger(Bootstrap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}