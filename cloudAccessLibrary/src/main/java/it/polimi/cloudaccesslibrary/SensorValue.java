package it.polimi.cloudaccesslibrary;

import java.util.ArrayList;

/**
 * Created by saeed on 5/8/2017.
 */

public class SensorValue {

    public static class Con
    {
        private double temperature;

        public double getTemperature() { return this.temperature; }

        public void setTemperature(double temperature) { this.temperature = temperature; }

        private double light;

        public double getLight() { return this.light; }

        public void setLight(double light) { this.light = light; }

        private int pressure;

        public int getPressure() { return this.pressure; }

        public void setPressure(int pressure) { this.pressure = pressure; }

        private int altitude;

        public int getAltitude() { return this.altitude; }

        public void setAltitude(int altitude) { this.altitude = altitude; }
    }

    public static class M2mCin
    {
        private Con con;

        public Con getCon() { return this.con; }

        public void setCon(Con con) { this.con = con; }

        private String cnf;

        public String getCnf() { return this.cnf; }

        public void setCnf(String cnf) { this.cnf = cnf; }

        private ArrayList<String> lbl;

        public ArrayList<String> getLbl() { return this.lbl; }

        public void setLbl(ArrayList<String> lbl) { this.lbl = lbl; }
    }

    public static class RootObject
    {
        private M2mCin m2m_cin;

        public M2mCin getM2mCin() { return this.m2m_cin; }

       public void setM2mCin(M2mCin m2m_cin) { this.m2m_cin = m2m_cin; }
    }

}


