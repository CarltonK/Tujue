package handler;


import retrofit2.Call;
import retrofit2.http.GET;


public interface InterfaceRequest {

    //Top Headlines
    @GET("v2/top-headlines?country=us&apiKey=bdb7bf453b3140498dc1928c6b639434")
    Call<ServerResponse> getHeadlines();

    //Technology
    @GET("v2/top-headlines?country=us&category=technology&apiKey=bdb7bf453b3140498dc1928c6b639434")
    Call<ServerResponse> getTechnology();

    //Health
    @GET("v2/top-headlines?country=us&category=health&apiKey=bdb7bf453b3140498dc1928c6b639434")
    Call<ServerResponse> getHealth();

    //Business
    @GET("v2/top-headlines?country=us&category=business&apiKey=bdb7bf453b3140498dc1928c6b639434")
    Call<ServerResponse> getBusiness();

    //Entertainment
    @GET("v2/top-headlines?country=us&category=entertainment&apiKey=bdb7bf453b3140498dc1928c6b639434")
    Call<ServerResponse> getEntertainment();

    //Science
    @GET("v2/top-headlines?country=us&category=science&apiKey=bdb7bf453b3140498dc1928c6b639434")
    Call<ServerResponse> getScience();

    //Sports
    @GET("v2/top-headlines?country=us&category=sports&apiKey=bdb7bf453b3140498dc1928c6b639434")
    Call<ServerResponse> getSports();

}
