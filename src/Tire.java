import java.io.Serializable;

public class Tire implements Serializable {

    String tireSize;
    String series;
    String tireSeason;//0:yaz , 1:kıs, 2:4 Mevsim

    String Brand;

    int productionYear;

    int quantity;



    public String getTireSeason() {
        return tireSeason;
    }

    public String getBrand() {
        return Brand;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public int getQuantity() {
        return quantity;
    }




    public Tire( String tireSize,String series, String tireSeason, String brand, int productionYear, int quantity) {


        this.tireSize=tireSizeConvertor(tireSize);
        this.series=series;
        this.tireSeason = tireSeason;
        Brand = brand;
        this.productionYear = productionYear;
        this.quantity=quantity;
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




    @Override
    public boolean equals(Object obj) {

        if(obj.getClass()!=Tire.class)
            return false;


        if(!tireSize.equalsIgnoreCase(((Tire)obj).tireSize))
            return false;
        if(!series.equalsIgnoreCase(((Tire)obj).series))
            return false;
        if(!tireSeason.equalsIgnoreCase(((Tire)obj).tireSeason))
            return false;
        if(!Brand.equalsIgnoreCase(((Tire)obj).Brand))
            return false;
        if(productionYear!=((Tire)obj).productionYear)
            return false;

        return true;


    }



}
