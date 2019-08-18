public class Users {//CLASS FOR USER POINTS

    public String points;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(Users.class)
    }

    public Users(String points) {
        this.points = points;
    }

    public String UsersPointsGET(){ //Getter method to extract the string points value
        return points;
    }

}
