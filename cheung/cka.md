总体来说只要好好复习基本不难, </br>
时间比较紧迫,要分配好时间, </br>
多读文档,保证能迅速的找到需要的知识点, </br>
常用的几个命令记熟了，</br>
并没有太多的时间来犹豫，</br>
考试的时候是可以访问官方文档的, </br>
推荐把重要的知识点都分类保存到标签栏, </br>
纸上谈来终是浅, </br>
考过了也要多实践 </br>
[kubernetes笔记](https://github.com/cheungYX/algorithm/blob/master/cheung/kubernetes.md)

# 概要
* [考试大纲](https://github.com/cncf/curriculum)
* 题数:24問
* 考试时间: 3小时
* 价格:300刀
  - 打折 https://www.goodsearch.org/coupons/the-linux-foundation?open=53827629
* 对于CKA考试，必须获得74％或以上的分数才能通过。
* 比例
  - Application Lifecycle Management 8%
  - Installation, Configuration & Validation 12%
  - Core Concepts 19%
  - Networking 11%
  - Scheduling 5%
  - Security 12%
  - Cluster Maintenance 11%
  - Logging / Monitoring 5%
  - Storage 7%
  - Troubleshooting 10%

# 复习材料
* [官网](https://kubernetes.io)
* [考试大纲](https://github.com/cncf/curriculum)
* [阿里云公开课](https://edu.aliyun.com/roadmap/cloudnative#teacher)
* [Geektime张磊课程](https://time.geekbang.org/column/article/0?cid=116)
* [imooc](https://coding.imooc.com/learn/list/335.html)
* [马哥运维进阶教程](https://www.bilibili.com/video/av82289390?p=1)
* [Kubernetes 指南](https://feisky.gitbooks.io/kubernetes/content/)
* [学习路径](https://github.com/caicloud/kube-ladder)
* [kubernetes the hard way](https://github.com/kelseyhightower/kubernetes-the-hard-way)
* [考试大纲知识点资料大全](https://github.com/walidshaari/Kubernetes-Certified-Administrator)
* [考题](https://blog.csdn.net/deerjoe/article/details/86300826)
* [英文原题](https://blog.csdn.net/fly910905/article/details/103652034)
* [华为云公开课](https://bbs.huaweicloud.com/forum/thread-11064-1-1.html)
* [资源大全](https://docs.google.com/spreadsheets/d/10NltoF_6y3mBwUzQ4bcQLQfCE1BWSgUDcJXy-Qp2JEU/edit#gid=0)
* [CKA考试知识总结](http://ljchen.net/2018/11/07/CKA%E8%80%83%E8%AF%95%E7%9F%A5%E8%AF%86%E6%80%BB%E7%BB%93/)

# 技巧
- 命令自动补全 source <(kubectl completion bash)
- 刷新浏览器会导致考试被终止
- 尽量使用命令创建Pod、deployment、service
  ``` 
  kubectl run podname --image=imagename --restart=Never -n namespace
  kubectl run <deploymentname> --image=<imagename> -n <namespace>
  kubectl expose <deploymentname> --port=<portNo.> --name=<svcname>
  ```
* 使用–dry-run
  ```
  kubectl run <podname> --image=<imagename> --restart=Never --dry-run -o yaml > title.yaml
  ```

# 真题
* 将所有pv按照name排序并输出结果到制定的文件中
  - kubectl get pv --sort-by=.metadata.name
* DaemonSet
* 启动一个包含nginx,redis,ubuntu的pod
  - kubectl run test --image=nginx --image=redis --image=ubutu
* deployment的升级，主要使用rollout
* deploy的扩容
  - kubectl scale deployment test --replicas=3
  - kubectl edit deploy test
* 给pod front-app创建对应的service将它暴露出来
  - kubectl expose pod valid-pod --port=444 --name=frontend
* 将deployment my-nginx绑定nodeport类型service，并且输出service和pod解析的dns日志
  - kubectl expose deployment my-nginx --type=NodePort --port=80 --target-port=80
  - kubectl exec -it busybox nslookup my-nginx
* pod redis挂载一个volume，挂载到目录/data/redis下，要求目录是non-presist的
* initcontainer
* 将pod nginx调度到label为disk=ssd的节点上:nodeselector 
* 将节点node1不参与调度，并将他所有的pod分配到其他node上
  - kubectl drain node1 --ignore-daemonsets --delete-local-data
* 统计集群中所有的可用节点，不包含不可调度的节点，将个数写到对应的文件中去
  - kubectl get nodes
  - kubectl describe nodename
* 列出pod中log为file-not-found的行并且写入到指定文件中
  - kubectl logs podname | grep file-not-found > f1.txt
* 找出service my-app对应的pod中使用cpu最高的pod，将pod名称写入到对应文件
  - kubectl get service -o wide
  - kubectl top node --selector=查出来的label
* 创建一个sercert 叫my-secret，内容为username=test,分别将他挂载到pod1的/data/secret,设为pod2的环境变量AUTHUSER
  - vi tmd.txt username=test
  - kubectl create secret generic my-secret --from-file=tmd.txt
* 使用etcd备份，将备份文件存放到指定路径，提供endpoints、ca、cert、key
* 配置节点启动的静态pod保证修改是永久的
* 解决节点启动不了的情况，并使改动是永久的
  - systemctl start kubelet
  - systemctl enable kubelet
* 将一个节点加入集群
* 解决集群中的问题
* update node by drain/cordon
* 必考
  - 日志挂载主机目录
  - deployment创建
  - deployment升级与回滚
  ```
  # 暂停
  kubectl rollout pause deployment/nginx-deployment
  # 恢复
  kubectl rollout resume deployment/nginx-deployment
  # 查询状态
  kubectl rollout status deployment/nginx-deployment
  # 查询历史
  kubectl rollout history deployment/nginx-deployment
  kubectl rollout history deployment/nginx-deployment  --revision=2
  # 回滚
  kubectl rollout undo deployment/nginx-deployment --to--revision=2
  ```
  - 创建service
    - kubectl create service clusterip my-svc --clusterip='None'
    - kubectl expose deployment hello-nginx --type=ClusterIP --name=my-nginx --port=8090 --target-port=80
  - DNS
    - wget https://kubernetes.io/examples/admin/dns/busybox.yaml
    - kubectl exec -it busybox  -- nsloopup kubernetes.default