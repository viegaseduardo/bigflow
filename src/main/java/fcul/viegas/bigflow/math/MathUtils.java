/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fcul.viegas.bigflow.math;

/**
 *
 * @author viegas
 */
public class MathUtils {
    
    private int count;
    private Long sum;
    private Long sumSq;
    
    public MathUtils(){
        this.count = 0;
        this.sum = 0l;
        this.sumSq = 0l;
    }
   
    
    public void addNumber(int number){
        this.count += 1;
        this.sum += number;
        this.sumSq += number*number;
    }
    
    public Float getAverage(){
        if(this.count == 0){
            return 0.0f;
        }
        return (this.sum/(float)this.count);
    }
    
    public double getVariance() {
        double deviation = this.getStandardDeviation();
        deviation = deviation*deviation;
        if(Double.isNaN(deviation)){
            return 0.0d;
        }
        return deviation;
    }
    
    public double getStandardDeviation(){
        double deviation = 0.0f;
        if(this.count > 1){
            deviation = Math.sqrt((this.sumSq - this.sum*this.sum/this.count)/((float)this.count-1));
        }
        if(Double.isNaN(deviation)){
            return 0.0d;
        }
        return deviation;
    }

    public int getCount() {
        return count;
    }

    public Long getSum() {
        return sum;
    }

}
