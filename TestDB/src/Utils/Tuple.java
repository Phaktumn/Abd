package Utils;

public class Tuple<T, R> {

    public T first;
    public R second;

     public static <T,R> Tuple<T, R> of(T t, R r){
         return new Tuple<>(t,r);
     }

     public static Tuple<?, ?> from(Tuple t){
         return of(t.first, t.second);
     }

    private Tuple(T t, R r){
        first = t;
        second = r;
    }
}
