package com.ch.resource_seore.util;


import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class CreateBasicData {
    static private final int defaultLength = 20;
    static private final int defaultMax = 100;
    static private final int defaultStringLength = 10;
    /**
     * 返回限定长度 限定大小 随机 int[]
     * 不限定默认值为 length：20 max:100
     * @return int[]
      */
    public static int[] createRandomIntArray() {
        return createRandomIntArray(defaultLength, defaultMax);
    }
    public static int[] createRandomIntArray(int length) {
        return createRandomIntArray(length, defaultMax);
    }
    public static int[] createRandomIntArray(int length, int max) {
        Random seed = new Random(System.currentTimeMillis());
        return seed.ints(0, max).limit(length).toArray();
    }

    /**
     * 返回限定长度 限定大小 随机的 List<Integer>
     *     不限定默认值为 length：20 max:100
     * @return List<Integer>
     */
    public static List<Integer> createRandomIntList() {
        return createRandomIntList(defaultLength,defaultMax);
    }
    public static List<Integer> createRandomIntList(int length) {
        return createRandomIntList(length,defaultMax);
    }
    public static List<Integer> createRandomIntList(int length,int max) {
        Random seed = new Random(System.currentTimeMillis());
        Supplier<Integer> random = () -> seed.nextInt(max);
        return Stream.generate(random).limit(length).collect(Collectors.toList());
    }
    /**
     * 返回限定长度 限定大小 随机的 Set<Integer>
     *     不限定默认值为 length：20 max:100
     * @return Set<Integer>
     */
    public static Set<Integer> createRandomIntSet() {
        return createRandomIntSet(defaultLength, defaultMax);
    }
    public static Set<Integer> createRandomIntSet(int length) {
        return createRandomIntSet(length, defaultMax);
    }
    public static Set<Integer> createRandomIntSet(int length,int max) {
        Random seed = new Random(System.currentTimeMillis());
        Supplier<Integer> random = () -> seed.nextInt(max);
        return Stream.generate(random).limit(length).collect(Collectors.toSet());
    }

    /**
     * 返回限定长度 限定大小 随机的 Set<String>
     *     不限定默认值为 length：20 max:10
     * @return Set<String>
     */
    public static Set<String> createRandomStringSet() {
        return createRandomStringSet(defaultLength,defaultStringLength);
    }
    public static Set<String> createRandomStringSet(int length) {
        return   createRandomStringSet(length,defaultStringLength);
    }
    public static Set<String> createRandomStringSet(int length,int max) {
      return   Stream.generate(new PersonSupplier(max)).limit(length).collect(Collectors.toSet());
    }
    /**
     * 返回限定长度 限定大小 随机的 List<String>
     *     不限定默认值为 length：20 max:10
     * @return List<String>
     */
    public static List<String> createRandomStringList() {
        return createRandomStringList(defaultLength,defaultStringLength);
    }
    public static List<String> createRandomStringList(int length) {
        return  createRandomStringList(length,defaultStringLength);
    }
    public static List<String> createRandomStringList(int length,int max) {
        return   Stream.generate(new PersonSupplier(max)).limit(length).collect(Collectors.toList());
    }
    /**
     * 返回限定长度 限定大小 随机 String[]
     * 不限定默认值为 length：20 max:10
     * @return String[]
     */
    public static String[] createRandomStringArray() {
        return createRandomStringArray(defaultLength,defaultStringLength);
    }
    public static String[] createRandomStringArray(int length) {
        return createRandomStringArray(length,defaultStringLength);
    }
    public static String[] createRandomStringArray(int length,int max) {
        return   (String[]) Stream.generate(new PersonSupplier(max)).limit(length).toArray();
    }

    public static <K,V> Map<K, V> createRandomMap(Set<K> keys, Collection<V> value) throws Exception {
        Map<K, V> map = new HashMap<>();
        if (keys.size() != value.size()) {
            throw new Exception("长度不符");
        }
        Iterator<K> itKey = keys.iterator();
        Iterator<V> itValue = value.iterator();
        while (itKey.hasNext()) {
            map.put(itKey.next(), itValue.next());
        }
        return map;
    }

    /**
     * 返回给定长度随机字符串
     * @param length
     * @return
     */
    public static String getRandomString(int length){
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }


    public static void main(String[] args) {
        testStringCollection( createRandomStringSet(20,20));
    }


    static public void testIntArray(int[] data) {
        System.out.println(Arrays.toString(data));
        System.out.println(data.length);
        System.out.println(data[0]);
        System.out.println(data[data.length - 1]);
    }
    static public void testIntCollection(Collection<Integer> data) {

        System.out.print("collection:  [");
        for (int i : data) {
            System.out.print(i +", ");
        }
        System.out.println("]");
        System.out.println(" size : " + data.size());

        data.removeAll(data);
        data.add(1002);
        data.add(1003);
        System.out.print("remove after collection:  [");
        for (int i : data) {
            System.out.print(i+", ");
        }
        System.out.println("]");
        System.out.println(" size : " + data.size());

    }
    static public void testStringCollection(Collection<String> data) {

        System.out.print("collection:  [");
        for (String i : data) {
            System.out.print(i +", ");
        }
        System.out.println("]");
        System.out.println(" size : " + data.size());

        data.removeAll(data);
        data.add("test data 1");
        data.add("test data 2");
        System.out.print("remove after collection:  [");
        for (String i : data) {
            System.out.print(i+", ");
        }
        System.out.println("]");
        System.out.println(" size : " + data.size());

    }
    public static class PersonSupplier implements Supplier<String> {

        private int length;

        public PersonSupplier(int length) {
            this.length = length;
        }

        @Override
        public String get() {
            return getRandomString(length);
        }
    }
}
