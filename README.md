HomeActivity ->  public boolean onOptionsItemSelected
우측상단 툴바가 눌렸을때 액티비티를 키는 함수, UnifiedNavigationActivity 실행
UnifiedNavigationActivity -> PrefsFragment 실행 -> setting.xml 호출
setting.xml -> 알고리즘 설정창 ==> 지워야함ㄴ
string.xml ->
    <string-array name="algoNames">
        <item name="1">K Nearest Neighbor (KNN) Algorithm</item>
        <item name="2">Weighted K Nearest Neighbor (WKNN) Algorithm</item>
        <item name="3">Probabilistic Maximum A Posteriori (MAP) Algorithm</item>
        <item name="4">Probabilistic Minimum Mean Square Error (MMSE) Algorithm</item>
    </string-array>
    <string-array name="algoValues">
        <item name="1">1</item>
        <item name="2">2</item>
        <item name="3">3</item>
        <item name="4">4</item>
    </string-array>  --> setting.xml에서 참조함. 지워야됨

menu_home.xml -> 메인화면 상단바
 <item
        android:id="@+id/settings"
        android:icon="@android:drawable/ic_menu_manage"
        android:title="Settings"
        app:showAsAction="ifRoom" /> --> 스패너 버튼임. 지움
        
       
      
LocateMeActivity - > 현재 자신의 위치를 찾는 화면. 알고리즘을 여기서 다룸
defaultAlgo -> 선택한 알고리즘 , getDefaultAlgo-> 선택한 알고리즘을 찾는 메소드/, Utils.java에 있음 --> 지움
==> 2로 고정시킴(wknn)
line 102 -> 알고리즘 실행
Algorithm.java --> 많이 잘라냈읍니다...
