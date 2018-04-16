
public class SampleTest {

  public static void main(String[] args) {
    Fascia[] fascias = new Fascia[8];
    Sku s1 = new Sku("1");
    Sku s2 = new Sku("2");
    Sku s3 = new Sku("3");
    Sku s4 = new Sku("1");
    Fascia f1 = new Fascia(s1, false, "Green", "SES");
    Fascia f2 = new Fascia(s2, false, "Black", "SS");
    Fascia f3 = new Fascia(s3, false, "White", "SE");
    Fascia f4 = new Fascia(s4, false, "Red", "CE");
    
    fascias[0] = f1;
    fascias[1] = f2;
    fascias[2] = f3;
    fascias[3] = f4;

    int[][] a = new int[4][2];
    
    int[] b = {1,2,3,4};
    int[] c = {5,6,7,8};
    for (int i=0; i < b.length; i++) {
      
     a[i][0] = b[i];
    }  
    
    for (int i=0; i < c.length; i++) {
      
      a[i][1] = c[i];
     }     
//    i[0][0] = 1;
//    i[1][0] = 2;
//    i[2][0] = 3;
//    i[3][0] = 4;
//    
//    i[0][1] = 5;
//    i[1][1] = 6;
//    i[2][1] = 7;
//    i[3][1] = 8;
    
    //System.out.println(i.length);
    //System.out.println(i[0].length);
    for (int j = 0; j <  a.length; j++) {
      System.out.println(a[j][0]);
    }
    for (int j = 0; j <  a.length; j++) {
      System.out.println(a[j][1]);
    }
    

    

    

  }

}
