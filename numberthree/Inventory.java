package numberthree;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<ItemStack> item;
    private int capacity = 36;//背包容量为36，无法更改

    public Inventory() {
    }

    public Inventory(ArrayList<ItemStack> item) {
        this.item = item;
    }

    public ArrayList<ItemStack> getItem() {
        return item;
    }

    public void setItem(ArrayList<ItemStack> item) {
        this.item = item;
    }

    public int getCapacity() {
        return capacity;
    }

    public int addItem(ItemStack itemStackToAdd) {
        judge : for (int i = 0; i < this.item.size(); i++) {
            //判断背包里是否存在添加的物品
            if (this.item.get(i).getItem().getName().equals(itemStackToAdd.getItem().getName())) {
                //如果存在添加的物品，判断背包里是否存在未填满的物品栏
                for (int i1 = 0; i1 < this.item.size(); i1++) {
                    if (this.item.get(i1).getItem().getName().equals(itemStackToAdd.getItem().getName()) && this.item.get(i1).getQuantity() != itemStackToAdd.getItem().getMaxStackSize()) {
                        //如果存在未填满的物品栏，判断该物品栏是否足够完全填入此次物品
                        if (this.item.get(i1).getQuantity() + itemStackToAdd.getQuantity() <= itemStackToAdd.getItem().getMaxStackSize()) {
                            //如果足够填入，则直接替代操作
                            ItemStack replaceItemStack = new ItemStack(itemStackToAdd.getItem(), this.item.get(i1).getQuantity() + itemStackToAdd.getQuantity());
                            this.item.set(i1, replaceItemStack);
                            //返回剩余背包容量
                            int remain = this.capacity - this.item.size();
                            return remain;
                        } else {
                            //否则，将其填满后进入后续操作
                            itemStackToAdd.setQuantity(itemStackToAdd.getQuantity() - (itemStackToAdd.getItem().getMaxStackSize() - this.item.get(i1).getQuantity()));
                            ItemStack replaceItemStack = new ItemStack(itemStackToAdd.getItem(), itemStackToAdd.getItem().getMaxStackSize());
                            this.item.set(i1, replaceItemStack);
                            break judge;
                        }

                    }
                }

            }
        }

                //当物品不存在或已存在物品均为最大容量时判断背包是否已满
                if(this.item.size()==36){
                    //当背包已满时直接返回物品数量的负数
                    int remain = (-1) * itemStackToAdd.getQuantity();
                    System.out.println("有"+itemStackToAdd.getQuantity()+"个"+itemStackToAdd.getItem().getName()+"未能成功放入");
                    return remain;
                }else {
                    //当背包未满时判断物品数量是否超过最大堆叠数量
                    if (itemStackToAdd.getQuantity() <= itemStackToAdd.getItem().getMaxStackSize()) {
                        //如果物品数量不超过最大堆叠数量，直接添加到背包
                        this.item.add(itemStackToAdd);
                        int remain = this.capacity-this.item.size();
                        return remain;
                    } else {
                        //记录能够产生的最大堆叠数量的物品栏数
                        int itemStackNumber;
                        itemStackNumber = itemStackToAdd.getQuantity() / itemStackToAdd.getItem().getMaxStackSize();
                        //记录产生的非最大堆叠数量的物品栏中，该物品栏中物品的数量
                        int itemRemainNumber;
                        itemRemainNumber = itemStackToAdd.getQuantity() % itemStackToAdd.getItem().getMaxStackSize();
                        //记录是否产生了非最大堆叠数量的物品栏
                        int itemRemainNumberStack;
                        if (itemRemainNumber==0){
                            itemRemainNumberStack =0;
                        }else {
                            itemRemainNumberStack =1;
                        }
                        //判断是否有物品栏无法添加
                        if(itemStackNumber+this.item.size()+itemRemainNumberStack>this.capacity){
                            //如果有，则添加至背包已满
                            for (int i1 = 0; i1 < (this.capacity - this.item.size()); i1++) {
                                ItemStack itemStack= new ItemStack(itemStackToAdd.getItem(),itemStackToAdd.getItem().getMaxStackSize());
                                this.item.add(itemStack);
                            }
                            //然后记录剩余多少物品未添加成功
                            int remain = 0;
                            remain = (itemStackNumber+this.item.size()-this.capacity)*itemStackToAdd.getItem().getMaxStackSize() + itemRemainNumber;
                            //变为负数，返回添加失败的物品数量
                            System.out.println("有"+remain+"个"+itemStackToAdd.getItem().getName()+"未能成功放入");
                            remain = (-1) * remain;
                            return remain;
                        }else {
                            //如果没有，则全部添加
                            for (int i1 = 0; i1 < itemStackNumber; i1++) {
                                ItemStack itemStack= new ItemStack(itemStackToAdd.getItem(),itemStackToAdd.getItem().getMaxStackSize());
                                this.item.add(itemStack);
                            }
                            ItemStack itemStack= new ItemStack(itemStackToAdd.getItem(),itemRemainNumber);
                            //记录剩余背包栏数并返回
                            int remain = this.capacity-this.item.size();
                            return remain;
                        }

                    }
                }




    }

    public void removeItem(String itemName, int amountToRemove){
        //判断移除的物品是否存在
        for (int i = 0; i < this.item.size(); i++) {
            if(itemName.equals(this.item.get(i).getItem().getName())){
                //如果物品存在,判断该格物品是否足够移除
                if(amountToRemove > this.item.get(i).getQuantity()){
                    //如果不够移除，更改剩余所需移除的个数，然后将该物品栏完全移除
                    amountToRemove = amountToRemove - this.item.get(i).getQuantity();
                    this.item.remove(i);
                }else {
                    //如果足够移除，则直接更改该物品栏的数量
                    Item item1 = new Item(itemName);
                    ItemStack itemStack = new ItemStack(item1,this.item.get(i).getQuantity()-amountToRemove);
                    this.item.set(i,itemStack);
                    return;
                }

            }

        }
        //如果移除数量大于剩余数量，则输出提示
        System.out.println("背包中已无该物品");
    }

    public  void getItemCount(String itemName){
        //定义变量记录数据
        int count = 0;
        for (int i = 0; i < this.item.size(); i++) {
            if(itemName.equals(this.item.get(i).getItem().getName())){
                //加上该物品在每个物品栏的数量
                count = count + this.item.get(i).getQuantity();
            }
        }
        System.out.println("背包中有"+count+"个"+itemName);
    }

    public  void display(){
        for (int i = 0; i < this.item.size(); i++) {
            System.out.print(this.item.get(i).getQuantity()+"个"+this.item.get(i).getItem().getName()+" ");
        }
        System.out.println();
    }

}