package DataManagementSystem;

import SharedDataTypes.*;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleSensorObject;
import SharedDataTypes.SensorObjects.SimpleVirtualSensorObject;
import SharedDataTypes.SensorObjects.VirtualSensorObject;

import javax.annotation.PreDestroy;
import java.io.FileInputStream;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBAccess {

    //region Database Connection
    /*****************Connect to DataBase*****************/
    public static Connection connectDB(){

        Connection dbConnection = null;
        Properties dbConnectionStringProperties = new Properties();

        try {
            FileInputStream inputStream = new FileInputStream("src/main/resources/db.properties");

            dbConnectionStringProperties.load(inputStream);

            inputStream.close();

            Class.forName(dbConnectionStringProperties.getProperty("driver-class-name")).newInstance();

            dbConnection = DriverManager.getConnection(dbConnectionStringProperties.getProperty("url"),
                    dbConnectionStringProperties.getProperty("username"),
                    dbConnectionStringProperties.getProperty("password"));
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        catch (Exception ex) {
            return null;
        }

        return dbConnection;

    }
    //endregion

    //region Sensor

    /*****************Add sensor to 'sensor' Table*****************/
    public static void addSensor(String serialNumber, String nickname, float sampleRate, int type,int status,String description,String location, float Max, float Min){

        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL add_sensor(?,?,?,?,?,?,?,?,?)}");

            //INPUT sensorIn SENSOR OBJECT PARAMETERS:
            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setString("nickname", nickname);
            dbStatement.setFloat("sampleRate", sampleRate);
            dbStatement.setInt("sensorType", type);
            dbStatement.setInt("sensorStatus", status);
            dbStatement.setString("description", description);
            dbStatement.setString("location", location);
            dbStatement.setFloat("max", Max);
            dbStatement.setFloat("min", Min);

            dbStatement.executeQuery();

        }
        catch (SQLException ex){
            System.out.println("ERROR IN - addSensor: ");

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }

    public static SimpleSensorObject check_sensor(String serialNumber){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        SimpleSensorObject sensorOut = new SimpleSensorObject();

        try {
            dbStatement = dbConnection.prepareCall("{CALL check_sensor(?)}");

            dbStatement.setString("serialNumber", serialNumber);

            dbResultSet = dbStatement.executeQuery();

            if (dbResultSet.next()) {
                sensorOut.name = dbResultSet.getString("serialNumber");
                sensorOut.nickName = dbResultSet.getString("nickname");
                sensorOut.sampleRate = dbResultSet.getFloat("sampleRate");
                sensorOut.type = SensorType.fromValue(dbResultSet.getInt("sensorType"));
                sensorOut.state = SensorState.fromValue(dbResultSet.getInt("sensorStatus"));
                sensorOut.description = dbResultSet.getString("description");
                sensorOut.location = dbResultSet.getString("location");
                sensorOut.max = dbResultSet.getFloat("MAX");
                sensorOut.min = dbResultSet.getFloat("MIN");
            }
        }
        catch (SQLException ex){
            System.out.println("ERROR IN - check_sensor: ");

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorOut;
    }

    /*****************Retrieve specific sensor from sensor table*****************/
    public static SimpleSensorObject getSensor(String serialNumber){

        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        SimpleSensorObject sensorOut = new SimpleSensorObject();

        try {
            dbStatement = dbConnection.prepareCall("{CALL get_sensor(?)}");

            dbStatement.setString("serialNumber", serialNumber);

            dbResultSet = dbStatement.executeQuery();

            if (dbResultSet.next()) {
                sensorOut.name = dbResultSet.getString("serialNumber");
                sensorOut.nickName = dbResultSet.getString("nickname");
                sensorOut.sampleRate = dbResultSet.getFloat("sampleRate");
                sensorOut.type = SensorType.fromValue(dbResultSet.getInt("sensorType"));
                sensorOut.state = SensorState.fromValue(dbResultSet.getInt("sensorStatus"));
                sensorOut.description = dbResultSet.getString("description");
                sensorOut.location = dbResultSet.getString("location");
                sensorOut.max = dbResultSet.getFloat("MAX");
                sensorOut.min = dbResultSet.getFloat("MIN");
            }
        }
        catch (SQLException ex){
            System.out.println("ERROR IN - getSensor: ");

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorOut;
    }

    /*****************List all sensors in sensor Table*****************/
    public static ArrayList<SimpleSensorObject> listSensors(){

        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<SimpleSensorObject> sensorOut = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL list_sensors()}");

            dbResultSet = dbStatement.executeQuery();
            sensorOut = new ArrayList<SimpleSensorObject>();
            while (dbResultSet.next()) {
                SimpleSensorObject temp = new SimpleSensorObject();

                temp.name = dbResultSet.getString("serialNumber");
                temp.nickName = dbResultSet.getString("nickname");
                temp.sampleRate = dbResultSet.getFloat("sampleRate");
                temp.type = SensorType.fromValue(dbResultSet.getInt("sensorType"));
                temp.state = SensorState.fromValue(dbResultSet.getInt("sensorStatus"));
                temp.description = dbResultSet.getString("description");
                temp.location = dbResultSet.getString("location");
                temp.max = dbResultSet.getFloat("MAX");
                temp.min = dbResultSet.getFloat("MIN");

                sensorOut.add(temp);
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException in listSensors" );

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorOut;
    }

    public static boolean update_sensor(SensorObject toChange){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL update_sensor(?,?,?,?,?,?,?,?,?)}");

            //INPUT sensorIn SENSOR OBJECT PARAMETERS:
            dbStatement.setString("serialNumber", toChange.name);
            dbStatement.setString("nickname", toChange.nickName);
            dbStatement.setFloat("sampleRate", toChange.sampleRate);
            dbStatement.setInt("sensorType", toChange.type.getValue());
            dbStatement.setInt("sensorStatus", toChange.state.getValue());
            dbStatement.setString("description", toChange.description);
            dbStatement.setString("location", toChange.location);
            dbStatement.setFloat("MAX", toChange.max);
            dbStatement.setFloat("MIN", toChange.min);

            dbStatement.executeQuery();

        }
        catch (SQLException ex){
            System.out.println("SQLException in update_sensor");

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return true;
    }

    public static boolean delete_sensor(String serialNumber){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL delete_sensor(?)}");

            //INPUT sensorIn SENSOR OBJECT PARAMETERS:
            dbStatement.setString("serialNumber", serialNumber);

            dbStatement.executeQuery();

        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return true;
    }

    public static ArrayList<SimpleSensorObject> request_user_sensors(String username){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<SimpleSensorObject> sensorOut = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL request_user_sensors(?)}");

            dbStatement.setString("username", username);

            dbResultSet = dbStatement.executeQuery();
            sensorOut = new ArrayList<SimpleSensorObject>();
            while (dbResultSet.next()) {
                SimpleSensorObject temp = new SimpleSensorObject();

                temp.name = dbResultSet.getString("serialNumber");
                temp.nickName = dbResultSet.getString("nickname");
                temp.sampleRate = dbResultSet.getFloat("sampleRate");
                temp.type = SensorType.fromValue(dbResultSet.getInt("sensorType"));
                temp.state = SensorState.fromValue(dbResultSet.getInt("sensorStatus"));
                temp.description = dbResultSet.getString("description");
                temp.location = dbResultSet.getString("location");
                temp.max = dbResultSet.getFloat("MAX");
                temp.min = dbResultSet.getFloat("MIN");

                sensorOut.add(temp);
            }
        }
        catch (SQLException ex){
            System.out.println("ERROR IN - request_user_sensors: ");

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }
                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }
                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorOut;
    }


    //endregion

    //region sensor_reading
    /*****************Add Sensor to sensor_reading Table*****************/
    public static boolean addSensor_reading(String serialNumber, double value, String time) {
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbstatement = null;

        try {
            dbstatement = dbConnection.prepareCall("{CALL add_sensor_reading(?,?,?)}");
            dbstatement.setString("serialNumber", serialNumber);
            dbstatement.setDouble("value", value);
            dbstatement.setTimestamp("timestamp", Timestamp.valueOf(time));

            dbstatement.executeQuery();
        } catch (SQLException ex) {
            System.out.println("ERROR IN - addSensor_reading: ");

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        } finally {
            if (dbstatement != null) {
                try {
                    dbstatement.close();
                } catch (SQLException ex) {
                }

                dbstatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return true;

    }

    /*****************Retrieve a specific sensor from sensor_reading Table*****************/
    public static boolean isAggregateSensor(String serialNumber){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        boolean isAggregate = false;

        try {
            dbStatement = dbConnection.prepareCall("{CALL is_aggregate_sensor(?)}");

            dbStatement.setString("serialNumber", serialNumber);

            dbResultSet = dbStatement.executeQuery();

            if (dbResultSet.next()) {
                isAggregate = dbResultSet.getBoolean("isAggregate");
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }
                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }
                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return isAggregate;
    }

    /*****************Convert a string expression into a SQL query equivalent expression*****************/
    private static String interpretExpression(String expression, String startTime, String endTime) {
        if (Objects.equals(startTime, "") && Objects.equals(endTime, "")) {
            StringBuilder expressionQuery = new StringBuilder();
            int sensorCount = 1;
            int idCount = 1;
            int lastIndex = 0;
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
            Matcher matcher = pattern.matcher(expression);

            while (matcher.find()) {
                idCount++;

                String sensor = matcher.group();
                String replacement = "(SELECT value FROM sensor_reading " + "s" + idCount + " WHERE " + "s" + idCount + ".serialNumber = " + "'" + sensor + "'" + " AND " + "s" + idCount + ".timestamp = s1.timestamp LIMIT 1)";
                expressionQuery.append(expression, lastIndex, matcher.start());
                expressionQuery.append(replacement);
                lastIndex = matcher.end();

                if (sensorCount < matcher.groupCount()) {
                    expressionQuery.append(" / ");
                    sensorCount++;
                }
            }

            if (lastIndex < expression.length()) {
                expressionQuery.append(expression.substring(lastIndex));
            }

            return "SELECT timestamp, COALESCE ((" + expressionQuery.toString() + "), -1) AS value FROM sensor_reading s1 GROUP BY timestamp;";
        }
        else if (Objects.equals(endTime, "")) {
            StringBuilder expressionQuery = new StringBuilder();
            int sensorCount = 1;
            int idCount = 1;
            int lastIndex = 0;
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
            Matcher matcher = pattern.matcher(expression);

            while (matcher.find()) {
                idCount++;

                String sensor = matcher.group();
                String replacement = "(SELECT value FROM sensor_reading " + "s" + idCount + " WHERE " + "s" + idCount + ".serialNumber = " + "'" + sensor + "'" + " AND " + "s" + idCount + ".timestamp = s1.timestamp LIMIT 1)";
                expressionQuery.append(expression, lastIndex, matcher.start());
                expressionQuery.append(replacement);
                lastIndex = matcher.end();

                if (sensorCount < matcher.groupCount()) {
                    expressionQuery.append(" / ");
                    sensorCount++;
                }
            }

            if (lastIndex < expression.length()) {
                expressionQuery.append(expression.substring(lastIndex));
            }

            return "SELECT timestamp, COALESCE ((" + expressionQuery.toString() + "), -1) AS value FROM sensor_reading s1 WHERE s1.timestamp >= " + "'" + Timestamp.valueOf(startTime).toString() + "'" + " GROUP BY timestamp;";
        }
        else if (startTime.equals("")) {
            StringBuilder expressionQuery = new StringBuilder();
            int sensorCount = 1;
            int idCount = 1;
            int lastIndex = 0;
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
            Matcher matcher = pattern.matcher(expression);

            while (matcher.find()) {
                idCount++;

                String sensor = matcher.group();
                String replacement = "(SELECT value FROM sensor_reading " + "s" + idCount + " WHERE " + "s" + idCount + ".serialNumber = " + "'" + sensor + "'" + " AND " + "s" + idCount + ".timestamp = s1.timestamp LIMIT 1)";
                expressionQuery.append(expression, lastIndex, matcher.start());
                expressionQuery.append(replacement);
                lastIndex = matcher.end();

                if (sensorCount < matcher.groupCount()) {
                    expressionQuery.append(" / ");
                    sensorCount++;
                }
            }

            if (lastIndex < expression.length()) {
                expressionQuery.append(expression.substring(lastIndex));
            }

            return "SELECT timestamp, COALESCE ((" + expressionQuery.toString() + "), -1) AS value FROM sensor_reading s1 WHERE s1.timestamp <= " + "'" + Timestamp.valueOf(endTime).toString() + "'" + " GROUP BY timestamp;";
        }
        else{
            StringBuilder expressionQuery = new StringBuilder();
            int sensorCount = 1;
            int idCount = 1;
            int lastIndex = 0;
            Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
            Matcher matcher = pattern.matcher(expression);

            while (matcher.find()) {
                idCount++;

                String sensor = matcher.group();
                String replacement = "(SELECT value FROM sensor_reading " + "s" + idCount + " WHERE " + "s" + idCount + ".serialNumber = " + "'" + sensor + "'" + " AND " + "s" + idCount + ".timestamp = s1.timestamp LIMIT 1)";
                expressionQuery.append(expression, lastIndex, matcher.start());
                expressionQuery.append(replacement);
                lastIndex = matcher.end();

                if (sensorCount < matcher.groupCount()) {
                    expressionQuery.append(" / ");
                    sensorCount++;
                }
            }

            if (lastIndex < expression.length()) {
                expressionQuery.append(expression.substring(lastIndex));
            }

            return "SELECT timestamp, COALESCE ((" + expressionQuery.toString() + "), -1) AS value FROM sensor_reading s1 WHERE s1.timestamp >= " + "'" + Timestamp.valueOf(startTime).toString() + "'" + " AND s1.timestamp <= " + "'" + Timestamp.valueOf(endTime).toString() + "'" + " GROUP BY timestamp;";
        }
    }

    /*****************Retrieve a specific sensor from sensor_reading Table*****************/
    public static SensorSample[] getSensor_reading(String serialNumber, String startTime, String endTime){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        int sensorReadingListSize = 0;
        int currentSensorReading = 0;
        SensorSample[] sensorSampleList = null;

        try{
            if (isAggregateSensor(serialNumber)) {
                // If getting data from an aggregate sensor

                //Get aggregation expression:
                String expression = DBAccess.getSensor(serialNumber).description;

                expression = interpretExpression(expression, startTime, endTime);

                dbStatement = dbConnection.prepareCall("{CALL sensor_aggregation_processor(?)}", dbResultSet.TYPE_SCROLL_INSENSITIVE, dbResultSet.CONCUR_READ_ONLY);

                dbStatement.setString("expression", expression);

                dbResultSet = dbStatement.executeQuery();

                if(dbResultSet.last()){
                    sensorReadingListSize = dbResultSet.getRow();
                    dbResultSet.beforeFirst();
                    sensorSampleList = new SensorSample[sensorReadingListSize];
                }

                while(dbResultSet.next()){
                    SensorSample sensorReading = new SensorSample();
                    sensorReading.value = (float) dbResultSet.getDouble("value");
                    sensorReading.sampleDateTime = dbResultSet.getString("timestamp");

                    sensorSampleList[currentSensorReading] = sensorReading;

                    currentSensorReading++;
                }

            }
            else{
                // If  getting data from a real sensor
                dbStatement = dbConnection.prepareCall("{CALL get_sensor_reading(?,?,?)}", dbResultSet.TYPE_SCROLL_INSENSITIVE, dbResultSet.CONCUR_READ_ONLY);

                dbStatement.setString("serialNumber", serialNumber);

                if(Objects.equals(startTime, "")){
                    dbStatement.setTimestamp("startTime", null);
                }
                else{
                    dbStatement.setTimestamp("startTime", Timestamp.valueOf(startTime));
                }
                if(Objects.equals(endTime, "")){
                    dbStatement.setTimestamp("endTime", null);
                }
                else{
                    dbStatement.setTimestamp("endTime", Timestamp.valueOf(endTime));
                }

                dbResultSet = dbStatement.executeQuery();

                if(dbResultSet.last()){
                    sensorReadingListSize = dbResultSet.getRow();
                    dbResultSet.beforeFirst();
                    sensorSampleList = new SensorSample[sensorReadingListSize];
                }

                while(dbResultSet.next()){
                    SensorSample sensorReading = new SensorSample();
                    sensorReading.value = (float) dbResultSet.getDouble("value");
                    sensorReading.sampleDateTime = dbResultSet.getString("timestamp");

                    sensorSampleList[currentSensorReading] = sensorReading;

                    currentSensorReading++;
                }
            }
        }

        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorSampleList;
    }

    /*****************List all sensors in sensor_readings Table*****************/
    public static ArrayList<SensorSample> listsensor_readings(){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbstatement = null;
        ResultSet dbResultSet = null;
        ArrayList<SensorSample> sensorOut = null;

        try{
            dbstatement = dbConnection.prepareCall("{CALL list_sensor_readings()}");

            dbResultSet = dbstatement.executeQuery();
            sensorOut = new ArrayList<SensorSample>();
            while(dbResultSet.next()){

                SensorSample temp = new SensorSample();
                temp.value = (float)dbResultSet.getDouble("value");
                temp.sampleDateTime = dbResultSet.getString("timestamp");
            }
        }

        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbstatement != null) {
                try {
                    dbstatement.close();
                }
                catch (SQLException ex) { }

                dbstatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorOut;
    }
    //endregion

    //region sensor_access
    public static void add_sensor_access(String serialNumber, String username){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        int roleID;
        try{
            dbStatement = dbConnection.prepareCall("{CALL add_sensor_access(?,?)}");

            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setString("username", username);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }


    public static ArrayList<String> list_user_access(String username) {
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<String> sensorOut = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL list_user_access(?)}");

            dbStatement.setString("username", username);
            dbResultSet = dbStatement.executeQuery();
            sensorOut = new ArrayList<String>();
            while(dbResultSet.next()){
                sensorOut.add(dbStatement.getString("serialNumber"));
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        } finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                } catch (SQLException ex) {
                }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorOut;
    }

    public static ArrayList<String> list_group_access(int roleGroupID) {
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<String> sensorOut = null;
        try {
            dbStatement = dbConnection.prepareCall("{CALL list_group_access(?)}");

            dbStatement.setInt("roleGroupID", roleGroupID);
            dbResultSet = dbStatement.executeQuery();
            sensorOut = new ArrayList<String>();

            while(dbResultSet.next()){
                sensorOut.add(dbResultSet.getString("serialNumber"));
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        } finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                } catch (SQLException ex) {
                }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorOut;
    }

    public static ArrayList<Integer> get_user_sensor_access_type(String serialNumber, String username, int accessType){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<Integer> sensorOut = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL get_user_sensor_access_type(?,?)}");

            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setString("username", username);

            dbResultSet = dbStatement.executeQuery();

            sensorOut = new ArrayList<Integer>();
            if (dbResultSet.next()) {
                sensorOut.add(dbResultSet.getInt("accessType"));
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorOut;
    }

    public static int get_group_sensor_access_type(String serialNumber, int roleGroupID){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        int sensorOut = 0;

        try {
            dbStatement = dbConnection.prepareCall("{CALL get_group_sensor_access_type(?,?)}");

            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setInt("roleGroupID", roleGroupID);

            dbResultSet = dbStatement.executeQuery();

            if (dbResultSet.next()) {
                sensorOut = dbResultSet.getInt("accessType");
            }
        }

        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return 0;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return sensorOut;
    }

    public static void delete_all_sensor_access(String serialNumber){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL delete_all_sensor_access(?)}");

            dbStatement.setString("serialNumber", serialNumber);

            dbStatement.executeQuery();

        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }

    public static boolean delete_user_sensor_access(String serialNumber, String username){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL delete_user_sensor_access(?,?)}");

            //INPUT sensorIn SENSOR OBJECT PARAMETERS:
            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setString("username", username);

            dbStatement.executeQuery();

        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return true;
    }

    public static boolean delete_group_sensor_access(String serialNumber, int roleGroupID){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL delete_group_sensor_access(?,?)}");

            //INPUT sensorIn SENSOR OBJECT PARAMETERS:
            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setInt("roleGroupIDx", roleGroupID);

            dbStatement.executeQuery();

        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return true;
    }

    public static boolean update_user_sensor_access(String serialNumber, String username){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL update_user_sensor_access(?,?)}");

            //INPUT sensorIn SENSOR OBJECT PARAMETERS:
            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setString("username", username);

            dbStatement.executeQuery();

        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return true;
    }

    public static boolean update_group_sensor_access(String serialNumber, int roleGroupID, int accessType){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL update_user_sensor_access(?,?,?)}");

            //INPUT sensorIn SENSOR OBJECT PARAMETERS:
            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setInt("roleGroupID", roleGroupID);
            dbStatement.setInt("accessType", accessType);

            dbStatement.executeQuery();

        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return true;
    }

    //endregion

    //region user
    public static void add_user(String username, String password){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL add_user(?,?)}");

            dbStatement.setString("username", username);
            dbStatement.setString("password", password);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        if (dbConnection != null) {
            try {
                dbConnection.close();
            }
            catch (SQLException ex) { }

            dbConnection = null;
        }
    }

    public static int check_role(String username){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        int roleOut = -1;

        try{
            dbStatement = dbConnection.prepareCall("{CALL check_role(?)}");
            dbStatement.setString("username", username);
            dbResultSet = dbStatement.executeQuery();


            if(dbResultSet.next()) {
                roleOut = dbResultSet.getInt("roleID");
            }

        }
        catch (SQLException ex) {
            System.out.println("SQLException in check_role");

            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return -1;
        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                } catch (SQLException ex) {
                }
                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                }

                dbConnection = null;
            }
        }
            return roleOut;
    }


    public static UserObject[] Get_Researcher(){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<UserObject> UsersOut = null;

        try{
            dbStatement = dbConnection.prepareCall("{CALL get_Researcher()}");
            dbResultSet = dbStatement.executeQuery();

            UsersOut = new ArrayList<>();
            while (dbResultSet.next()){
                UserObject temp = new UserObject();
                temp.userID = dbResultSet.getInt("profileID");
                temp.userName = dbResultSet.getString("username");
                temp.name = dbResultSet.getString("name");
                temp.email = dbResultSet.getString("email");
                temp.role = dbResultSet.getString("roleID");
                UsersOut.add(temp);
            }

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        UserObject[] out = new UserObject[UsersOut.size()];
        for(int i=0; i<UsersOut.size(); i++){
            out[i] = UsersOut.get(i);
        }
        return out;
    }

    public static UserObject[] Get_Researcher(String serialNumber){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<UserObject> UsersOut = null;

        try{
            dbStatement = dbConnection.prepareCall("{CALL get_Reserchers_of_sensor(?)}");
            dbStatement.setString("serialNumber", serialNumber);
            dbResultSet = dbStatement.executeQuery();

            UsersOut = new ArrayList<>();
            while (dbResultSet.next()){
                UserObject temp = new UserObject();
                temp.userID = dbResultSet.getInt("profileID");
                temp.userName = dbResultSet.getString("username");
                temp.name = dbResultSet.getString("name");
                temp.email = dbResultSet.getString("email");
                temp.role = dbResultSet.getString("roleID");
                UsersOut.add(temp);
            }

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        UserObject[] out = new UserObject[UsersOut.size()];
        for(int i=0; i<UsersOut.size(); i++){
            out[i] = UsersOut.get(i);
        }
        return out;
    }

    /* use get_user_authority instead
    public static boolean check_user(String username, String password){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL check_user(?,?)}");

            dbStatement.setString("username", username);
            dbStatement.setString("password", password);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }
            if (dbConnection != null) {
            try {
                dbConnection.close();
            }
            catch (SQLException ex) { }

            dbConnection = null;
        }
        }
        return true;
    }*/

    public static int get_user_authority(String username, String password){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        int userAuthority = -1;

        try{
            dbStatement = dbConnection.prepareCall("{CALL get_user_authority(?,?)}");

            dbStatement.setString("user", username);
            dbStatement.setString("password", password);

            dbResultSet = dbStatement.executeQuery();

            if (dbResultSet.next()){
                userAuthority = dbResultSet.getInt("roleID");
            }

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return -1;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return userAuthority;
    }

    public static boolean delete_user(String username, String password){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL delete_user(?,?)}");

            dbStatement.setString("username", username);
            dbStatement.setString("password", password);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return true;
    }

    public static boolean update_user_password(String username, String password){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL update_user_password(?,?)}");

            dbStatement.setString("username", username);
            dbStatement.setString("password", password);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return true;
    }
    //endregion

    //region roleGroupID

    public static String get_role_group(int roleGroupID){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        String roleGroupName = "None";

        try{
            dbStatement = dbConnection.prepareCall("{CALL get_role_group(?)}");

            dbStatement.setInt("roleGroupID", roleGroupID);

            dbResultSet = dbStatement.executeQuery();

            if(dbResultSet.next()){
                roleGroupName = dbResultSet.getString("name");
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return roleGroupName;
    }

    //endregion

    //region user profile
    public static boolean add_user_profile(UserObject toAdd){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL add_user_profile(?,?,?,?)}");

            dbStatement.setString("username", toAdd.userName);
            dbStatement.setString("name", toAdd.name);
            dbStatement.setString("email", toAdd.email);
            dbStatement.setString("roleGroupID", toAdd.role);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return true;
    }

    public static boolean delete_user_profile(String username, String password){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL delete_user_profile(?,?)}");

            dbStatement.setString("username", username);
            dbStatement.setString("password", password);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return true;
    }

    public static UserObject get_user_profile(String username, String password){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        UserObject userOut = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL get_user_profile(?,?)}");

            dbStatement.setString("username", username);
            dbStatement.setString("password", password);

            dbResultSet = dbStatement.executeQuery();

            userOut = new UserObject();
            if (dbResultSet != null) {
                userOut.userID =  dbResultSet.getInt("profileID");
                userOut.userName = dbResultSet.getString("username");
                userOut.name = dbResultSet.getString("name");
                userOut.email = dbResultSet.getString("email");
                userOut.role = dbResultSet.getString("name");
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return userOut;
    }

    public static boolean update_user_profile(String username, String name, String email, int roleID){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL update_user_profile(?,?,?,?,?)}");

            dbStatement.setString("username", username);
            dbStatement.setString("name", name);
            dbStatement.setString("email", email);
            dbStatement.setInt("roleGroupID", roleID);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return true;
    }

    //endregion

    //region ticket
    public static boolean add_ticket(TicketObject toAdd){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL add_ticket(?,?,?,?,?,?)}");

            dbStatement.setString("fieldNotes", toAdd.fieldNotes);
            dbStatement.setString("serialNumber", toAdd.sensorInfo.name);
            dbStatement.setString("creationDate", toAdd.creationDate);
            dbStatement.setInt("ticketState", toAdd.ticketState.getValue());
            dbStatement.setString("assignedUser", toAdd.assignedUserID);
            dbStatement.setInt("assignedTimeSlot", toAdd.assignedTimeSlot);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException in add_ticket");
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return true;
    }

    public static TicketObject[] get_nonassigned_sensor_tickets(){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        TicketObject[] ticketOut = null;
        TicketObject tickets = null;
        VirtualSensorObject sensorOut = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL get_non_assigned_sensor_tickets()}");

            dbResultSet = dbStatement.executeQuery();
            ticketOut = new TicketObject[dbResultSet.getFetchSize()];
            int i=0;
            while(dbResultSet.next()){
                tickets = new TicketObject();
                tickets.ticketID = dbResultSet.getInt("ticketID");
                tickets.fieldNotes = dbResultSet.getString("fieldNotes");
                tickets.creationDate = dbResultSet.getString("creation_date");
                tickets.sensorInfo = DBAccess.getSensor(dbResultSet.getString("serialNumber"));
                tickets.assignedUserID = null;
                tickets.ticketState = TicketState.fromValue(dbResultSet.getInt("ticketState"));

                ticketOut[i] = tickets;
                i++;
            }

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally{
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return ticketOut;
    }

    public static void assign_user_to_ticket(String username, TicketObject toAssign){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL assign_ticket_to_user(?,?)}");

            dbStatement.setString("username", username);
            dbStatement.setInt("ticketID", toAssign.ticketID);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }

    public static void update_ticket_status(int ticketID, int progress_status){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL update_ticket_status(?,?)}");

            dbStatement.setInt("progress_status", progress_status);
            dbStatement.setInt("ticketID", ticketID);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }

    public static void update_ticket(TicketObject toRecord){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL update_ticket(?,?,?,?,?,?)}");

            dbStatement.setString("serialNumber", toRecord.sensorInfo.name);
            dbStatement.setString("user_assigned", toRecord.assignedUserID);
            dbStatement.setString("description", toRecord.fieldNotes);
            dbStatement.setInt("ticketID", toRecord.ticketID);
            dbStatement.setString("creationDate", toRecord.creationDate);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }

    public static void delete_ticket(int ticketID){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL delete_ticket(?)}");

            dbStatement.setInt("ticketID", ticketID);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }

    public static ArrayList<TicketObject> get_ticket(String serialNumber){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<TicketObject> ticketList = new ArrayList<>();

        try{
            dbStatement = dbConnection.prepareCall("{CALL get_ticket(?)}");

            dbStatement.setString("serialNumber", serialNumber);

            dbResultSet = dbStatement.executeQuery();


            while (dbResultSet.next()) {
                TicketObject tickets = new TicketObject();
                tickets.ticketID = dbResultSet.getInt("ticketID");
                tickets.fieldNotes = dbResultSet.getString("fieldNotes");
                tickets.creationDate = dbResultSet.getString("creation_date");
                tickets.sensorInfo = DBAccess.getSensor(dbResultSet.getString("serialNumber"));
                tickets.assignedUserID = dbResultSet.getString("user_assigned");
                tickets.ticketState = TicketState.fromValue(dbResultSet.getInt("progress_status"));
                tickets.assignedTimeSlot = dbResultSet.getInt("assignedTimeSlot");
                ticketList.add(tickets);
            }


        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally{
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return ticketList;
    }

    public static TicketObject[] get_sensor_tickets(String sensorID){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        TicketObject[] listOut = null;

        try{
            dbStatement = dbConnection.prepareCall("{CALL get_sensor_ticket(?)}");

            dbStatement.setString("serialNumber", sensorID);

            dbResultSet = dbStatement.executeQuery();

            listOut = new TicketObject[dbResultSet.getFetchSize()];
            TicketObject temporary = null;
            int i=0;

            while(dbResultSet.next()){
                temporary = new TicketObject();
                temporary.ticketID = dbResultSet.getInt("ticketID");
                temporary.fieldNotes = dbResultSet.getString("fieldNotes");
                temporary.sensorInfo = DBAccess.getSensor(dbResultSet.getString("serialNumber"));
                temporary.creationDate = dbResultSet.getString("creation_date");
                TicketState TS = TicketState.fromValue(dbResultSet.getInt("progress_status"));
                temporary.ticketState = TS;
                temporary.assignedUserID = dbResultSet.getString("user_assigned");
                temporary.assignedTimeSlot = dbResultSet.getInt("AssignedTimeSlot");
                listOut[i] = temporary;
                i++;
            }

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally{
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return listOut;
    }

    public static TicketObject[] list_tickets() {
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<TicketObject> listOut = new ArrayList<>();

        try {
            dbStatement = dbConnection.prepareCall("{CALL list_tickets()}");

            dbResultSet = dbStatement.executeQuery();
            System.out.println("Chris wants us to see this: "+dbResultSet);
            TicketObject temporary = null;
            int i = 0;

            while (dbResultSet.next()) {
                temporary = new TicketObject();
                temporary.ticketID = dbResultSet.getInt("ticketID");
                temporary.fieldNotes = dbResultSet.getString("fieldNotes");
                temporary.sensorInfo = DBAccess.getSensor(dbResultSet.getString("serialNumber"));
                temporary.creationDate = dbResultSet.getString("creation_date");
                TicketState TS = TicketState.fromValue(dbResultSet.getInt("progress_status"));
                temporary.ticketState = TS;
                temporary.assignedUserID = dbResultSet.getString("user_assigned");
                temporary.assignedTimeSlot = dbResultSet.getInt("AssignedTimeSlot");
                listOut.add(temporary);

            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        } finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        TicketObject[] result = new TicketObject[listOut.size()];
        for (int i = 0; i < listOut.size(); i++) {
            result[i] = listOut.get(i);
        }
        return result;
    }

    public static TicketObject[] get_user_tickets(String username){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<TicketObject> listOut = new ArrayList<>();

        try{
            dbStatement = dbConnection.prepareCall("{CALL get_user_tickets(?)}");

            dbStatement.setString("username", username);

            dbResultSet = dbStatement.executeQuery();
            TicketObject temporary = null;

            while(dbResultSet.next()){
                temporary = new TicketObject();
                temporary.ticketID = dbResultSet.getInt("ticketID");
                temporary.fieldNotes = dbResultSet.getString("fieldNotes");
                temporary.sensorInfo = DBAccess.getSensor(dbResultSet.getString("serialNumber"));
                temporary.creationDate = dbResultSet.getString("creation_date");
                TicketState TS = TicketState.fromValue(dbResultSet.getInt("progress_status"));
                temporary.ticketState = TS;
                temporary.assignedUserID = dbResultSet.getString("user_assigned");
                temporary.assignedTimeSlot = dbResultSet.getInt("AssignedTimeSlot");


                listOut.add(temporary);
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        TicketObject[] result = new TicketObject[listOut.size()];
        for (int i = 0; i < listOut.size(); i++) {
            result[i] = listOut.get(i);
        }
        return result;
    }

    //endregion

    //region sensor_aggregation

    public static void add_sensor_aggregation(String serialNumber, String nickname, int sensorType, int sensorStatus, String description, String Equation){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL add_sensor_aggregation(?,?,?,?,?,?)}");

            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setString("nickname", nickname);
            dbStatement.setInt("sensorType", sensorType);
            dbStatement.setInt("sensorStatus", sensorStatus);
            dbStatement.setString("Equation", Equation);
            dbStatement.setString("description", description);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }

    public String Get_Sensor_Aggregation_Value(String VirtualSensorID){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        String result = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL add_sensor_aggregation(?)}");

            dbStatement.setString("serialNumber", VirtualSensorID);
            dbResultSet = dbStatement.executeQuery();
            result = dbResultSet.getString("Equation");
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return result;
    }

    public static boolean delete_sensor_aggregation(String serialNumber){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL delete_sensor_aggregation(?)}");

            dbStatement.setString("serialNumber", serialNumber);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return true;
    }



    //endregion

    //region sensor log
    public static boolean add_sensor_log(String serialNumber, String description){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        try{
            dbStatement = dbConnection.prepareCall("{CALL add_sensor_log(?,?)}");

            dbStatement.setString("serialNumber", serialNumber);
            dbStatement.setString("description", description);

            dbStatement.executeQuery();

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return false;
        }
        finally{
            if(dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch(SQLException ex){}
                dbStatement = null;
            }

            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
        return true;
    }

    public static ArrayList<String> list_sensor_log(){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<String> listOut = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL list_sensors()}");

            dbResultSet = dbStatement.executeQuery();
            String temp = "";
            listOut = new ArrayList<String>();
            while (dbResultSet.next()) {
                temp = dbResultSet.getString("serialNumber") + ": " + dbResultSet.getString("description");
                listOut.add(temp);
            }
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

            return null;
        }
        finally {
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        return listOut;
    }
    //endregion

    //region Schedule

    public static String Get_Schedule(String username){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        String result = "";

        try{
            dbStatement = dbConnection.prepareCall("{CALL get_schedule(?)}");

            dbStatement.setString("username", username);

            dbResultSet = dbStatement.executeQuery();

            if(dbResultSet.next()){
                result += dbResultSet.getInt("AM_8") + ";";
                result += dbResultSet.getInt("AM_9") + ";";
                result += dbResultSet.getInt("AM_10") + ";";
                result += dbResultSet.getInt("AM_11") + ";";
                result += dbResultSet.getInt("PM_12") + ";";
                result += dbResultSet.getInt("PM_1") + ";";
                result += dbResultSet.getInt("PM_2") + ";";
                result += dbResultSet.getInt("PM_3") + ";";
                result += dbResultSet.getInt("PM_4") + ";";
                result += dbResultSet.getInt("PM_5") + ";";
                result += dbResultSet.getInt("PM_6") + ";";
                result += dbResultSet.getInt("PM_7") + ";";
            }
            return result;


        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        finally{
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }

    public static UserSchedule[] get_all_schedules(){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;
        ResultSet dbResultSet = null;
        ArrayList<UserSchedule> list = new ArrayList<>();

        try{
            dbStatement = dbConnection.prepareCall("{CALL get_schedule()}");

            dbResultSet = dbStatement.executeQuery();

            while(dbResultSet.next()){
                String result = "";
                result += dbResultSet.getInt("AM_8") + ";";
                result += dbResultSet.getInt("AM_9") + ";";
                result += dbResultSet.getInt("AM_10") + ";";
                result += dbResultSet.getInt("AM_11") + ";";
                result += dbResultSet.getInt("PM_12") + ";";
                result += dbResultSet.getInt("PM_1") + ";";
                result += dbResultSet.getInt("PM_2") + ";";
                result += dbResultSet.getInt("PM_3") + ";";
                result += dbResultSet.getInt("PM_4") + ";";
                result += dbResultSet.getInt("PM_5") + ";";
                result += dbResultSet.getInt("PM_6") + ";";
                result += dbResultSet.getInt("PM_7") + ";";

                UserSchedule temp = new UserSchedule(dbResultSet.getString("username"), result);
                list.add(temp);
            }

        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            return null;
        }
        finally{
            if (dbResultSet != null) {
                try {
                    dbResultSet.close();
                }
                catch (SQLException ex) { }

                dbResultSet = null;
            }

            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }

        UserSchedule[] res = new UserSchedule[list.size()];
        for(int i=0; i<list.size(); i++){
            res[i] = list.get(i);
        }
        return res;
    }


    public static void update_Schedule(String username, String[] schedule){
        Connection dbConnection = DBAccess.connectDB();
        CallableStatement dbStatement = null;

        try {
            dbStatement = dbConnection.prepareCall("{CALL updateSchedule(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            dbStatement.setString("username", username);
            dbStatement.setInt("s8", Integer.parseInt(schedule[0]));
            dbStatement.setInt("s9", Integer.parseInt(schedule[1]));
            dbStatement.setInt("s10", Integer.parseInt(schedule[2]));
            dbStatement.setInt("s11", Integer.parseInt(schedule[3]));
            dbStatement.setInt("s12", Integer.parseInt(schedule[4]));
            dbStatement.setInt("s1", Integer.parseInt(schedule[5]));
            dbStatement.setInt("s2", Integer.parseInt(schedule[6]));
            dbStatement.setInt("s3", Integer.parseInt(schedule[7]));
            dbStatement.setInt("s4", Integer.parseInt(schedule[8]));
            dbStatement.setInt("s5", Integer.parseInt(schedule[9]));
            dbStatement.setInt("s6", Integer.parseInt(schedule[10]));
            dbStatement.setInt("s7", Integer.parseInt(schedule[11]));


            dbStatement.executeQuery();
        }
        catch (SQLException ex){
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
        finally {
            if (dbStatement != null) {
                try {
                    dbStatement.close();
                }
                catch (SQLException ex) { }

                dbStatement = null;
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                }
                catch (SQLException ex) { }

                dbConnection = null;
            }
        }
    }
    //endregion

    //region sensor_aggregation

    // SerialNumber, nickname, sensorType, sensorStatus, description, Equation =====> for VirtualSensor

    //store virtual sensor

    //retrieve sensor

    //endregion

    //region Test
    /*****************Main Method for testing purposes*****************/
    public static void test(String[] args){
        //TestDataBase(){

        //region sensor test
        System.out.println("------------Test 1: Adding a sensor to 'sensor' table------------");
        boolean check = false;  //not an actual statement, just replaced with true to avoid errors after testing
        //check= addSensor("wffgwg", 1, 1, "dxxxx", "building");
        if(check){
            System.out.println("Successfully added sensor");
        }
        else{
            System.out.println("Error adding sensor");
        }
        System.out.println();

        System.out.println("------------Test 2, Get First Sensor Info Row------------");
        SimpleSensorObject obj1 = new SimpleSensorObject();
        obj1 = getSensor("1");
        if(obj1 == null){
            System.out.println("No sensor Found");
        }
        else{
            System.out.println("username: " + obj1.name + " - sensor type: " + obj1.type + " sensor status: " + obj1.state + " description: " + obj1.description + " - location: " + obj1.location);
        }
        System.out.println();

        System.out.println("------------Test 3, Get Arbitrary Sensor Info Row------------");
        obj1 = getSensor("2");
        if(obj1 == null){
            System.out.println("No sensor Found");
        }
        else {
            System.out.println("username: " + obj1.name + " - sensor type: " + obj1.type + " sensor status: " + obj1.state + " description: " + obj1.description + " - location: " + obj1.location);
        }
        System.out.println();

        System.out.println("------------Test 4, Get Non-Existent Sensor Info Row------------");
        getSensor("5");
        if(obj1 == null){
            System.out.println("No sensor Found");
        }
        else {
            System.out.println("username: " + obj1.name + " - sensor type: " + obj1.type + " sensor status: " + obj1.state + " description: " + obj1.description + " - location: " + obj1.location);
        }
        System.out.println();

        System.out.println("------------Test 5, Get All Sensor Info Rows------------");
        ArrayList<SimpleSensorObject> list = listSensors();
        for(int i=0; i<list.size(); i++){
            System.out.println("username: " + list.get(i).name + " - sensor type: " + list.get(i).type + " sensor status: " + list.get(i).state + " description: " + list.get(i).description + " - location: " + list.get(i).location);

        }
        System.out.println();
        //endregion

        //region sensor reading test
        System.out.println("------------Test 6, adding a sensor_reading to 'sensor_reading' table------------");
        check = addSensor_reading("wffgwg", 12.1, "2023-10-22 10:53:20");
        if(check == true){
            System.out.println("Successfully added sensor_reading");
        }
        else{
            System.out.println("Error adding sensor_reading row");
        }
        System.out.println();

        System.out.println("------------Test 7: get a specific sensor_reading info------------");
        SensorSample[] listx = getSensor_reading("WUNVWP", "", "");
        for(int i=0; i<listx.length; i++){
            System.out.println("Value: " + listx[i].value + " - Data and time: "+ listx[i].sampleDateTime);
        }
        System.out.println();

        System.out.println("------------Test 8: Get all sensor_reading info rows------------");
        ArrayList<SensorSample> list1 = listsensor_readings();
        for(int i=0; i<list1.size(); i++){
            System.out.println("Value: " + list1.get(i).value + " - Data and time: "+ list1.get(i).sampleDateTime);
        }
        System.out.println();
        //endregion

        //region sensor access
        System.out.println("------------Test 9------------");
        //endregion

    }

    //endregion

    private static VirtualSensorObject convertToVirtual(SensorObject toConvert){
        VirtualSensorObject reply = new SimpleVirtualSensorObject();

        reply.name = toConvert.name;
        reply.nickName = toConvert.nickName;
        reply.samples = toConvert.samples;
        reply.state = toConvert.state;
        reply.type = toConvert.type;
        reply.description = toConvert.description;
        reply.location = toConvert.location;
        reply.sampleRate = toConvert.sampleRate;
        reply.max = toConvert.max;
        reply.min = toConvert.min;
        return reply;
    }

}
