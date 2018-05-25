package com.shellshellfish;

import static com.google.common.collect.ComparisonChain.start;
import static org.junit.Assert.assertTrue;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.google.common.reflect.Invokable;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service.Listener;
import com.google.common.util.concurrent.Service.State;
import com.shellshellfish.service.ThreadService;

import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

  /**
   * Rigorous Test :-)
   */
  @Test
  public void shouldAnswerWithTrue() {
    assertTrue(true);
  }

  @Test
  public void guavaTest() {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);

    Map<String, String> map = new HashMap<>();
    map.put("1", "1");
    map.put("2", "2");

    String join = Joiner.on(",").join(map.keySet());
    System.out.println(join);

    String join1 = Joiner.on(",").skipNulls().join("1", null, "2");
    String join2 = Joiner.on(",").useForNull("NONE").join("1", null, "2");
    System.out.println(join2);

    String join3 = Joiner.on("#").withKeyValueSeparator("=").join(map);
    System.out.println(join3);


  }

  @Test
  public void Splliter() {
    Iterable<String> split = Splitter.on(",").split("1,2,3");
    Iterable<String> split1 = Splitter.fixedLength(3).split("123456789123");

    print(split);
    print(split1);

  }

  private static void print(Iterable<String> split) {
    Iterator<String> iterator = split.iterator();
    while (iterator.hasNext()) {
      //System.out.println(split.iterator().next());
      System.out.println(iterator.next());
    }
  }

  @Test
  public void cacheTest() {
    LoadingCache<String, String> caches = CacheBuilder.newBuilder().maximumSize(100)
        .expireAfterWrite(2L, TimeUnit.SECONDS).build(
            new CacheLoader<String, String>() {
              @Override
              public String load(String s) throws Exception {
                System.out.println("11111");
                return s;
              }
            });
    try {
      String hello = caches.get("hello");

      Thread.sleep(5000);

      System.out.println(caches.get("hello"));
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void cacheTest2() {
    LoadingCache<String, String> caches = CacheBuilder.newBuilder().maximumSize(100)
        .refreshAfterWrite(2, TimeUnit.SECONDS).build(
            new CacheLoader<String, String>() {
              @Override
              public String load(String s) throws Exception {
                return s;
              }
            });
  }

  @Test
  public void orderTest() {
    List<Integer> items = new ArrayList<>();
    items.add(1);
    items.add(5);
    items.add(4);
    items.add(2);
    items.add(3);
    Ordering<Integer> ordering = Ordering
        .natural().onResultOf(new Function<Integer, Comparable>() {
          @Override
          public Comparable apply(Integer integer) {
            if ((integer & 1) == 0) {
              return integer * 2;
            }
            return integer;

          }

        });
    List<Integer> copy = ordering.sortedCopy(items);
    System.out.println(Arrays.toString(copy.toArray()));
    System.out.println(Objects.hashCode(13));
    System.out.println();


  }

  @Test
  public void testSet() {
    ImmutableSet<Integer> immutableSet = ImmutableSet.of(1, 2, 3);
    immutableSet.forEach(n -> System.out.println(n));
  }

  @Test
  public void primiviteTest() {
    final int[] a = {1, 2, 3, 4, 56};
    List<Integer> list = Ints.asList(a);
    CharMatcher a1 = CharMatcher.is('a');
    list.forEach(integer -> System.out.println(integer));
    /*new Thread(() -> {
      try {
        Thread.sleep(1000);
        System.out.println("aaaa");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();*/
  }

  @Test
  public void testLambda() {
    ImmutableList<String> immutableList = ImmutableList.of("java", "C++", "go", "python");
    immutableList.parallelStream().filter(s -> s.startsWith(""));

    //test(immutableList, (s -> s.startsWith("j")));
  }

  private static void test(ImmutableList<String> i, Predicate<String> predicate) {
    i.forEach(o -> {
      //System.out.println(o instanceof  String);
      if (predicate.test(o)) {
        System.out.println(o);
      }
    });
  }


  @Test
  public void testStack() {
    //System.out.println(1 / 0);
  }

  @Test
  public void testService() throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(1);
    ThreadService service = new ThreadService(countDownLatch);
    service.addListener(new Listener() {
      @Override
      public void starting() {
        System.out.println("服务开始");
      }

      @Override
      public void running() {
        System.out.println("服务运行");
      }

      @Override
      public void stopping(State from) {
        System.out.println("服务结束");
        System.out.println(from.name());
      }


      @Override
      public void terminated(State from) {
        System.out.println("服务终止");
        System.out.println(from.name());
      }


      @Override
      public void failed(State from, Throwable failure) {
        System.out.println("服务失败");
        System.out.println(failure.getMessage());
      }


    }, MoreExecutors.directExecutor());

    service.startAsync().awaitRunning();

    Thread.sleep(3000);
    //service.startAsync().awaitTerminated();
    System.out.println("service state:" + service.state());
    //  countDownLatch.await();
  }

  @Test
  public void testRefletion() throws NoSuchMethodException {
    ArrayList<String> stringList = Lists.newArrayList();
    ArrayList<Integer> intList = Lists.newArrayList();
    System.out.println("intList type is " + intList.getClass());
    System.out.println("stringList type is " + stringList.getClass());
    System.out.println(stringList.getClass().isAssignableFrom(intList.getClass()));

    TypeToken<ArrayList<String>> token = new TypeToken<ArrayList<String>>() {
    };
    TypeToken<?> typeToken = token.resolveType(token.getType());
    System.out.println(token.getType());
    Invokable<?, Object> resolveType = Invokable
        .from(token.getClass().getMethod("resolveType", Type.class));

  }

  @Test
  public void test(){
    Set<Integer> set = new TreeSet<>();
    set.add(3);
   // set.add(1);
    //set.add(3);
    set.add(2);
   // set.add(1);
    Integer next = set.iterator().next();
    System.out.println(next);
  }



}
