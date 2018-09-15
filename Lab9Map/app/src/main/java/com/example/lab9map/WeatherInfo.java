package com.example.lab9map;

/**
 * Created by 雷神 on 2017/11/27.
 */

public class WeatherInfo {
        String city,provice,time;
        String state,wind;
        String nowTem,nowState;

        public String getCity() {
            return city;
        }
        public void setCity(String city) {
            this.city = city;
        }
        public String getProvice() {
            return provice;
        }
        public void setProvice(String provice) {
            this.provice = provice;
        }
        public String getTime() {
            return time;
        }
        public void setTime(String time) {
            this.time = time;
        }
        public String getState() {
            return state;
        }
        public void setState(String state) {
            this.state = state;
        }
        public String getWind() {
            return wind;
        }
        public void setWind(String wind) {
            this.wind = wind;
        }
        public String getNowTem() {
            return nowTem;
        }
        public void setNowTem(String nowTem) {
            this.nowTem = nowTem;
        }
        public String getNowState() {
            return nowState;
        }
        public void setNowState(String nowState) {
            this.nowState = nowState;
        }
        @Override
        public String toString() {
            return  provice + city+"市\n"+time +"\n"+ state +"\n"+ wind +"\n"+  nowTem +"\n"+ nowState ;
        }
}
