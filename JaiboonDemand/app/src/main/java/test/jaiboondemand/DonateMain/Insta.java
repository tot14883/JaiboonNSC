package test.jaiboondemand.DonateMain;

public class Insta {
    private String title,desc,image,descproduct,nameproduct,priceproduct,imageproduct,uid,Name,amount,keyproduct,timepost,country;
    public Insta(){
    }
    public Insta(String country,String descproduct,String amount,String title,String desc,String image,String nameproduct,String priceproduct,String imageproduct,String uid,String Name,String keyproduct,String timepost){
        this.country = country;
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.uid = uid;
        this.Name = Name;
        this.nameproduct = nameproduct;
        this.priceproduct = priceproduct;
        this.imageproduct = imageproduct;
        this.amount = amount;
        this.keyproduct = keyproduct;
        this.descproduct = descproduct;
        this.timepost = timepost;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public String getTimepost() {
        return timepost;
    }

    public void setTimepost(String timepost) {
        this.timepost = timepost;
    }

    public void setDescproduct(String descproduct) {
        this.descproduct = descproduct;
    }

    public String getDescproduct() {
        return descproduct;
    }

    public String getKeyproduct() {
        return keyproduct;
    }

    public void setKeyproduct(String keyproduct) {
        this.keyproduct = keyproduct;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getImageproduct() {
        return imageproduct;
    }

    public String getNameproduct() {
        return nameproduct;
    }

    public String getPriceproduct() {
        return priceproduct;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public void setImageproduct(String imageproduct) {
        this.imageproduct = imageproduct;
    }

    public void setNameproduct(String nameproduct) {
        this.nameproduct = nameproduct;
    }

    public void setPriceproduct(String priceproduct) {
        this.priceproduct = priceproduct;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
