package listObject;

/**
 * Created by Administrator on 2015/8/25.
 */
public class Member {
    private int memberId;
    private String memberName;

    public Member(){

    }

    public void setMemberId(int memberId){
        this.memberId=memberId;
    }

    public void setMemberName(String memberName){
        this.memberName=memberName;
    }

    public int getMemberId(){
        return this.memberId;
    }

    public String getMemberName(){
        return this.memberName;
    }
}
