import java.io.Serializable;
import java.util.ArrayList;

public class Stocks implements Serializable {

    ArrayList<Tire> tires=new ArrayList<>();

    int tireCount=0;




    void addTire(String tireSize,String series, String tireSeason, String brand, int productionYear, int quantity){

        tireSize=tireSizeConvertor(tireSize);

        if(tireSize==null)
            return;

        tireCount+=quantity;

        int index=isExist(tireSize,series, tireSeason, brand, productionYear);

        if(index==-1)
            tires.add(new Tire(tireSize,series, tireSeason,  brand,  productionYear,quantity));
        else
            tires.get(index).quantity=tires.get(index).quantity+quantity;

    }

    //isExist method returns -1 if it does not exist, returns index if exist that tire

    int isExist(String tireSize,String series, String tireSeason, String brand, int productionYear){

        tireSize=tireSizeConvertor(tireSize);

        if(tireSize==null)
            return -1;

        Tire temp=new Tire(tireSize,series, tireSeason,  brand,  productionYear,1);

        for (int i=0;i<tires.size();i++){


            if(tires.get(i).equals(temp))
                return i;

        }
        return -1;

    }

    String tireSizeConvertor(String tireSize){

        if(tireSize==null || tireSize.isEmpty())
            return null;

        String helper="";

        for(int i=0;i<tireSize.length();i++) {
            if(tireSize.charAt(i)>='0' &&tireSize.charAt(i)<='9')
                helper += tireSize.charAt(i);
        }

        if(helper.length()!=7 && helper.length()!=5)
            return null;

        String ans="";
        if(helper.length()==7)
            ans=helper.substring(0,helper.length()-4)+"/"+helper.substring(helper.length()-4,helper.length()-2)+"R"+helper.substring(helper.length()-2);
        if(helper.length()==5)
            ans=helper.substring(0,3)+"R"+helper.substring(3);


        return ans;//205/55R15

    }


}
