# Visitor
这是一个快速访问集合（collection和map）并支持在迭代过程中进行增删改查的 java库。同时访问服务VisitService可以快速相互转化（collection和map之间）.

## 特点   
* 1， 快速迭代collection 和 map集合(同时支持crud and fiter ,不管迭代过程多复杂). 
* 2,  支持打包zip操作。
* 3,  支持集合排序(sort), 连接(join),分组（group）, 倒转(reverse)等。
* 4,  支持Collection集合 and  Map集合之间相互转换。
* 5,  可快速开发 callback管理器。
* 6,  链式编程。
  
## 此框架的目的/作用.
* 需求： 
     - 将一个Int类型的数组 元素作为key 转化成一个map, 然后得到map的value集合. 条件是迭代过程中需要将某些元素过滤或者删除。(ps: 过滤意思是该元素不放入map中，删除意思是从原集合上删除该元素)
* 实现
     - 传统的代码 实现代码大致是这样的。
``` java
public void testTransform0_traditional_optimize() {
		final int size = 10;
           //创建最初的list/array
		List<Integer> list = new ArrayList<Integer>();
		//add ten element
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		//转化为 map
		java.util.Map<Integer, String> map = new HashMap<Integer, String>();
		final ListIterator<Integer> it = list.listIterator();
		for( ;it.hasNext();){
			Integer val = it.next();
			if(val == 2){
				continue; //filter 2
			}else if(val == 5){
				it.remove(); //delete 5
				continue;
			}
			
			map.put(val, "" + val * 100);
		}
		
		//map -> collection
		Collection<String> values = map.values();
		
		assertEquals(values.size(), size - 2);
	}
```
     - 用 Visitor框架，实现，大致是这样的
```java
public void testTransform0() {
		final int size = 10;
		List<Integer> list = new ArrayList<Integer>();
		//add ten element
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		CollectionVisitService<String> service2 = VisitServices.from(list)
		   .beginOperateManager().filter(new               PredicateVisitor<Integer>() {
				@Override
				public Boolean visit(Integer t, Object param) {
					return t == 2; //filter(skip) 2
				}
	
			}).delete(new PredicateVisitor<Integer>() {
				@Override
				public Boolean visit(Integer t, Object param) {
					return t == 5; //delete 5
				}
			}).end()
			.transformToMapAsKeys(new ResultVisitor<Integer, String>() {
				@Override
				public String visit(Integer t, Object param) {
					return "" + t * 100;
				}
			}).transformToCollectionByValues();
		
		assertEquals(getSize(service2), size - 2);
	}
```
    - 看出来了吧，没错。这个框架就是采用链式编程结构 快速操作 collection和map. 并在之间可以相互转化。
     并且添加一些groovy的特性
 * callback manager ? so easy.
 ```java
 private static class CallbackManager {
		final ListVisitService<Callback> mService;

		public CallbackManager() {
			this.mService = VisitServices.from(new ArrayList<Callback>());
		}

		public void register(Callback cl) {
			mService.addIfNotExist(cl);
		}

		public int getSize() {
			return mService.size();
		}

		public void unregister(Callback cl) {
			mService.removeIfExist(cl);
		}

		public void dispatchCallback(String msg) {
			mService.fire(new FireVisitor<Callback>() {
				@Override
				public Boolean visit(Callback callback, Object param) {
					assertEquals(param, sMsg);
					callback.callback(param.toString());
					return null;
				}
			});
		}

 ```
     
## Gradle config

```java
   dependencies {
       compile 'com.heaven7.java.visitor:Visitor:1.1.7'
   }
```

## Api 说明 
* 简单的只有一句话:  只要记住 VisitServices这个类就可以了（就像jdk的Executors），
其他的都是辅助的（比如Visitors.类）。
* 详细说明.
   - 目前支持的操作。
```java
/** 删除 --- 迭代过程中删除 */
	public static final int OP_DELETE = 1;
	/** 过滤 --- 迭代过程中指定的 元素或者 键值对（key-value）不参加 本次调用(比如CollectionVisitService的 visitForQueryResultList  是一次调用)*/
	public static final int OP_FILTER = 2;
	/** 更新--- 迭代过程中进行更新  元素或者 键值对（key-value） */
	public static final int OP_UPDATE = 3;
      /** 插入---迭代过程中插入(set集合暂不支持)或者迭代完成后插入 */
	public static final int OP_INSERT = 4;
	
	/**
	 * 整理 trim---- 目前只支持map. 作用于迭代后 
	 */
	public static final int OP_TRIM   = 5;
```  
其中(1), 迭代过程中的操作（OP_DELETE , OP_FILTER, OP_UPDATE,OP_INSERT）是可以排序的。 打个比方。默认情况下。delete操作访问在 filter之前。那么如果delete了, 后面的操作就不再访问此 元素或者key-value.  ( ps 注意: trim操作是在迭代后访问的。)
如果要改变拦截 可以通过这个api.  CollectionVisitService/MapVisitService. beginIterateControl().first(...).second(...).end().

   (2). 同样的，迭代过程中的操作还支持--- 拦截后面的操作。 默认情况下。
只有delete, filter可以拦截。你可以调用api去改变 拦截属性. CollectionVisitService/MapVisitService.beginIterateControl().interceptIfSuccess(int operate)
  - [完整的api说明] (https://github.com/LightSun/Visitor/blob/master/README_API_CN.md)
  - 示例代码：(更多的示例在源代码test包里面。)
```java
// 下面的函数调用过程。首先是迭代过程中加了filter操作，然后访问里面的每个
// 元素Student获取一个list的Student2.
public void testForResult(){
		
		final int size = getStudentSize();
		final List<Student2> list = new ArrayList<Student2>();
		
		mService.beginOperateManager()
		//just fiter the student of id = 1
			.filter(new PredicateVisitor<Student>() {
				@Override
				public Boolean visit(Student t, Object param) {
					assertEquals("testForResult", param);
					return t.getId() == 1;
				}
			}).end()
		.visitForResultList("testForResult", Visitors.truePredicateVisitor(), 
				new ResultVisitor<Student, Student2>() {
					@Override
					public Student2 visit(Student t, Object param) {
						assertEquals("testForResult", param);
						if( t.getId() == 2){
							return null; //null means ignored.
						}
						return new Student2(t);
					}
				}, list);
		
		assertEquals(getStudentSize(), size);
		assertEquals(list.size(), size - 2 );
	}
```
    - 文档正在更多请稍后..




## License

    Copyright 2017  
                    heaven7(donshine723@gmail.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



