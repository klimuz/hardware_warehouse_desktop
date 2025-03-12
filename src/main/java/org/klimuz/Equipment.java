package org.klimuz;

import java.util.ArrayList;

public class Equipment {

    private String name;
    private int totalQuantity;
    private int inStock = 0;
    private ArrayList<Integer> jobsInfo = new ArrayList<>();

    public Equipment(String name, int totalQuantity) {
        this.name = name;
        this.totalQuantity = totalQuantity;
    }

    public String getName() {
        return name;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getInStock() {
        inStock = totalQuantity - getInUse();
        return inStock;
    }

    public int getJobsInfo(int jobIndex) {
        int equipmentQuantity = 0;
        if (!jobsInfo.isEmpty() && jobIndex < jobsInfo.size()) {
            equipmentQuantity = jobsInfo.get(jobIndex);
        }
        return equipmentQuantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setJobsInfo(int equipmentQuantity){
        jobsInfo.add(equipmentQuantity);
    }

    public void updateJobsInfo(int jobIndex, int equipmentQuantity) {
        jobsInfo.set(jobIndex, equipmentQuantity);
    }

    public void returnToStock(int jobIndex, int quantity){
        int inThisJob = getJobsInfo(jobIndex);
        if (quantity <= inThisJob){
            inThisJob -= quantity;
            jobsInfo.set(jobIndex, inThisJob);
            inStock += quantity;
        }
    }

    public int getInUse() {
        int inUse = 0;
        if (!jobsInfo.isEmpty()) {
            for (Integer q : jobsInfo) {
                inUse += q;
            }
        }
        return inUse;
    }

    public ArrayList<Integer> getJobsList(){
        return jobsInfo;
    }

    public void removeJob(int index){
        jobsInfo.remove(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Equipment equipment = (Equipment) obj;
        return name.equals(equipment.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
