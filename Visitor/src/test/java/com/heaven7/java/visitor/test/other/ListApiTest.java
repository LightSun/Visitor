package com.heaven7.java.visitor.test.other;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class ListApiTest {

	public static void main2(String[] args) {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5); //
		Integer[] array = list.toArray((Integer[]) Array.newInstance(Integer.class, 0));
		System.out.println(Arrays.toString(array));

		ListIterator<Integer> it = list.listIterator(); // 也是按顺序的
		while (it.hasNext()) {
			System.out.println(it.next());
		}
	}

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		
		//list = Arrays.asList(1, 2, 3, 4, 5); //asList返回 固定大小的list, 增加删除会报异常
		//UnsupportedOperationException 

		ListIterator<Integer> it = list.listIterator();
		while (it.hasNext()) {
			Integer cur = it.next(); //这个方法只会放回原来list的元素。新增的不在此次迭代中
			System.err.println(cur);
			if (cur >= 2) {
				// it.previous or next都会移动游标. 前移或者后移
				//System.out.println("before change: cur = " + cur + " ,pre = " 
				//+ it.previous() + " ,next = " + it.next());
				 it.add(0);
				 it.add(9);
				//System.out.println("after change: cur = " + cur + " ,pre = " + it.previous() + " ,next = " + it.next());
			}
			System.out.println("======================================");
		}
		System.out.println(list);
		
		//list.addAll(Arrays.asList()); //ok
	}

}
