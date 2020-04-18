package com.mcdomainname.marketplace;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Market {

   private ArrayList<Posting> listOfPostings;

    public Market() {
        //probably need to write it to a file so people don't lose their stuff.

        //Create a data structure to store the items for sale.  This structure should store the following:
        //  1. The seller
        //  2. The item stack for sale
        //  3. The item they want in return
        //  4. The quantity they want
        //Method to
        boolean tryToOpenFile = true;

        listOfPostings = new ArrayList<Posting>();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream("market.file");
        } catch (FileNotFoundException e) {
            System.out.println("Valid market.file not found.");
            tryToOpenFile = false;
        }if (tryToOpenFile) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                System.out.println(ChatColor.BLUE + "Reading in the list.");
                listOfPostings = (ArrayList) ois.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(!listOfPostings.isEmpty()){
            System.out.println(ChatColor.BLUE + "Printing the list.");
            printPostings();
        }
    }

    public void addPosting(ItemStack itemStackToPost, String owner, String materialDesired, int quantityOfDesiredItem) {
        listOfPostings.add(new Posting(itemStackToPost, owner, materialDesired, quantityOfDesiredItem));
    }

    public int getSize(){
        return listOfPostings.size();
    }

    public boolean isEmpty(){
        return listOfPostings.isEmpty();
    }

    public Posting getPosting(int i){
        return listOfPostings.get(i);
    }

    public ItemStack getAndRemovePosting(int i) { return listOfPostings.remove(i).getItemStack();}

    public void printPostings(){
/*        ArrayList<Posting> copyOfList = (ArrayList<Posting>) listOfPostings.clone();
        Iterator<Posting> iter = copyOfList.iterator();
        System.out.println("Postings:");
        while(iter.hasNext()){
            Posting post = iter.next();
            System.out.println(ChatColor.AQUA + post.getItemStackToPost().getType().name() + " " + post.getOwner() + " " +
                    " " + post.getMaterialDesired() + " " + post.getQuantityOfDesiredItem());
        }*/
        for (Posting posting : listOfPostings) {
            System.out.println(posting);
        }
    }

    public void saveMarket(){
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("market.file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            oos.writeObject(listOfPostings);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}