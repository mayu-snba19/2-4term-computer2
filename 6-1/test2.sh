java -jar junit-platform-console-standalone-1.7.0.jar -cp . --disable-banner --details=tree -c FindTokenTest
if [ $? -eq 0 ]
then
    echo "テストは成功しました"
else
    echo "テストは失敗しました"
fi