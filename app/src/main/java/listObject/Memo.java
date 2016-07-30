package listObject;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Memo {
    private String context;
    private int id;

    public Memo(){

    }

    public void setId(int id){
        this.id=id;
    }

    public void setContext(String context){
        this.context=context;
    }

    public String getContext(){
        return this.context;
    }

    public int getId(){
        return this.id;
    }

}
