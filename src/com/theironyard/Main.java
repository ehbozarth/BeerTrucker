package com.theironyard;

import com.sun.tools.internal.ws.processor.model.Model;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ArrayList<Beer> beerList = new ArrayList();

        //Anonymous Get Function
        Spark.get(
                "/",
                ((request, response) -> {
                    Session session = request.session();
                    String userName = session.attribute("user_name");
                    if(userName == null){
                        return new ModelAndView(new HashMap(), "not-logged-in.html");
                    }//End of if userName == null

                    HashMap m = new HashMap();
                    m.put("user_name", userName);
                    m.put("beers", beerList);
                    return  new ModelAndView(m, "logged-in.html");
                }),
                new MustacheTemplateEngine()
        );//End of Get "/" Main page

        Spark.post(
                "/login",
                ((request, response) -> {
                    String userName = request.queryParams("user_name");
                    Session session = request.session();
                    session.attribute("user_name", userName);
                    response.redirect("/");
                    return "";
                })
        );//End of Spark.post() /Login

        Spark.post(
                "/create-beer",
                ((request, response) -> {
                    Beer beer = new Beer();
                    beer.id = beerList.size() + 1;
                    beer.name = request.queryParams("beer_name");
                    beer.type = request.queryParams("beer_type");
                    beerList.add(beer);
                    response.redirect("/");
                    return "";
                })
        );//End of Spark.post() /Create-Beer

        Spark.post(
                "/delete-beer",
                ((request, response) -> {
                    String id = request.queryParams("beer_id");
                    try {
                        int idNum = Integer.valueOf(id);
                        beerList.remove(idNum-1);
                        for(int i = 0; i < beerList.size(); i++){
                            beerList.get(i).id = i + 1;
                        }
                    } catch (Exception e){

                    }
                    response.redirect("/");
                    return "";
                })
        );//End of Spark.post() /Delete-Beer



    }//End of Main Method

}//End of Main Class
