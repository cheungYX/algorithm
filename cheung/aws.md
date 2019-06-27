[問題集](https://aws.koiwaclub.com) 有料
[クラウドサービス活用資料集](https://aws.amazon.com/jp/aws-jp-introduction/)
[受験記](https://qiita.com/chihiro/items/efad41f83a954f12ec8f)
[AWS Summit Tokyo 2017 セッション資料・動画一覧](https://aws.amazon.com/jp/summit2017-report/details/)
[Developers.IOの再入門シリーズ](https://dev.classmethod.jp/series/aws-re-introduction-2018/)
[要点](https://qiita.com/yutaChaos/items/2b0b8d9bfe76a597953c)
https://www.certmetrics.com/amazon/default.aspx
[デザインパータン](http://aws.clouddesignpattern.org/index.php/メインページ)
[Well-Architected](https://aws.amazon.com/jp/architecture/well-architected/)
[ナレッジセンター](https://aws.amazon.com/jp/premiumsupport/knowledge-center/)
[料金計算](http://calculator.s3.amazonaws.com/index.html?lng=ja_JP)

本
https://www.amazon.co.jp/dp/4774176737?tag=qiitak-22
https://www.amazon.co.jp/dp/4822277372
https://www.udemy.com/aws-certified-solutions-architect-associate/
https://blog.mmmcorp.co.jp/blog/2018/07/06/how_to_pass_saa/

参考
https://snofra.hatenablog.com/entry/2018/07/10/191243
https://www.slideshare.net/AmazonWebServicesJapan/presentations
https://qiita.com/yutaChaos/items/2b0b8d9bfe76a597953c
https://qiita.com/riekure/items/9f178eef397ae12766c1
https://stay-ko.be
http://jayendrapatil.com/aws-solution-architect-associate-exam-learning-path/

# 範囲
EC2、ELB、Auto Scaling、
EBS、S3、RDS、DynamoDB、ElastiCache、
VPC、Route 53、
CloudWatch、CloudTrail、CloudFormation、
IAM、
SNS/SQS、

合格ライン 720 / 1000
130分
65問

* 回復性の高いアーキテクチャを設計する 34% 17問
* パフォーマンスに優れたアーキテクチャを定義する 24% 12問
* セキュアなアプリケーションおよびアーキテクチャを規程する 26% 13問
* コスト最適化アーキテクチャを設計する 10% 5問
* オペレーショナルエクセレンスを備えたアーキテクチャを定義する 6% 3問


51%

1.0 Designing highly available, cost-efficient, fault-tolerant, scalable systems: 48%
2.0 Implementation/Deployment: 83%
3.0 Data Security: 36%
4.0 Troubleshooting: 75%


% 56 => 72
1.0 Design Resilient Architectures: 66% 9 => 77%
2.0 Define Performant Architectures: 57% 7 => 85$
3.0 Specify Secure Applications and Architectures: 50% 6
4.0 Design Cost-Optimized Architectures: 0% 2 => 50%
5.0 Define Operationally-Excellent Architectures: 100% 1


* AWSアーキテクチャのベストプラクティス
* 価格/コストを含むクライアント要件に応じた開発(オンデマンド/リザーブド/スポット,RTO,PRO DRデザイン)
* アーキテクチャトレードオフの決定(高可用性とコストのトレードオフ)
* ハイブリットITアーキテクチャ(Direct Connect, Storage Gateway, VPC)
* 伸縮自在性とスケーラビリティ(Auto Scaling, SQS, ELB, CloudFront)

* 7つのベストプラクティス
  - 故障に備えた設計で障害を回避
  - コンボーネット間を疎結合で柔軟に
  - 伸縮自在性を実装
  - 全ての層でセキュリティを強化
  - 制約を恐れない
  - 処理の並列化を考慮
  - 様々なストレージの選択肢を活用

# コスト最適化
* ソリューションのコストを考慮
  * フルマネージドサービスはないか
* 目標に応じてリソースをサイジングしていますか
* コスト効率を高めるために適切な購入オプションの検討
  * 他のリージョンも検討、レイテンシはCloudFrontで対応
  * RI適用箇所を検討
  * AutoScaling
  * バッファペース(SQS Kinesisを活用)
* データ転送量
  * CloudFrontの活用
  * リザーブドキャパシティ割引
* コスト管理

# Storage Options
* S3(スケーラブルなストレージ), Glacier(低価格なアーカイブストレージ)
* EBS, Instance Store Volumes
* RDS, DynamoDB, Database on EC2
* SQS, Redshift
* CloudFront & Elasticache
* Storage Gateway & Import/Export

# 高可用性と耐障害性

# computing
## EC2 (Elastic Compute Cloud)
* リージョン
  - 地域 
* アベイラビリティゾーン
  - 物理的電力的独立
* リージョンサービスとAZサービスとグロバールサービス  
* プレスメントグループ
  - 単一AZゾーン、ネットワーク接続を高速化する 
* ユーザーデータとメタデータの違い
  - ユーザーデータはインスタンスの起動時にコマンドを渡すことができる
  - メタデータはインスタンスのpublic IP,Instance Typeなどのインスタンスの各種情報を取得することができる
* オンデマンドインスタンス
  - 通常で購入するインスタンスのこと
* リザーブドインスタンスとスポットインスタンスの活用
  - リザーブドインスタンスは1年、3年単位でインスタンスを予約して購入することで安く利用できる
  - スポットインスタンスは入札ベースでインスタンスを購入し、安く利用できる
* KMSのキーペアをSSHの鍵にできる
* AMI(Amazon Machine Image)
  - AMIは指定したregionのみで使用できる
  - 暗号化
* ライフサイクル
  - Running
  - Stopped
  - Terminated
* インスタンスストアは内臓のディスク(無料)
  - ブロックレベルの一時的ストレージ
  - インスタンスタイプごとに仕様が決まってる
  - 高速I/O 大容量
* 基盤にしているソフトウェアは Xen
* SLA 99.95%
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-2017-amazon-ec2)
* /dev/sad1 => ルート
* インスタンス内から http://169.254.169.254/


## IAM
* AWS操作を権限管理周りの仕組み
* 責任分担セキュリティモデル
  - インフラストラクチャサービス
  - コンテナサービス(プラットフォームとアプリケーション管理もawsの責務)
  - アブストラクトサービス(ネットワークサーバサイトの暗号化もaws)
* IAM roleとIAMユーザー
* ロール、ユーザー、グループの違い
* IAMで使う認証情報の種類について
  - アクセスキーID
  - シークレットアクセスキー
  - パスワード
  - MFA(多要素認証)
* IDフェデレーション
　- 一時的な許可 
* 最小責任で設定することがベストプラクティス
* デフォルトの設定は全拒否
* [IAM のベストプラクティス](https://docs.aws.amazon.com/ja_jp/IAM/latest/UserGuide/best-practices.html)
  - AWS アカウントのルートユーザーのアクセスキーをロック
  - 個々の IAM ユーザーの作成
  - IAM ユーザーへのアクセス権限を割り当てるためにグループを使用する
  - AWS 定義のポリシーを使用して可能な限りアクセス権限を割り当てる
  - 最小権限を付与する
  - アクセスレベルを使用して、IAM 権限を確認する
  - ユーザーの強力なパスワードポリシーを設定
  - 特権ユーザーに対して MFA を有効化する
  - Amazon EC2 インスタンスで実行するアプリケーションに対し、ロールを使用する
  - 認証情報を共有するのではなく、ロールを使って委託する
  - 認証情報を定期的にローテーションする
  - 不要な認証情報を削除する
  - 追加セキュリティに対するポリシー条件を使用する
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/20150617-aws-blackbeltiam)

## VPC (Virtual Private Cloud)
- vpc: Virtual Private Cloud
- コンボーネット
  - IGW(インタネット接続)
  - VGW(拠点と接続)
  - VPCピア接続
  - VPCE(awsサービスとの接続)
- CIDR: Classless Inter-Domain Routing 
  - 10.0.0.0 – 10.255.255.255 (10/8 プレフィックス)
  - 172.16.0.0-172.31.255.255 (172.16/12 プレフィックス)
  - 192.168.0.0 – 192.168.255.255 (192.168/16 プレフィックス)
* ゲートウェイ
  - インタネットの出入り口 
  - VGW
* ルートテーブル
  - iptableみたいなもの
  - VPC には暗示的なルーターがあります
  - テーブル内の各ルートは送信先 CIDR とターゲットを指定します
* subnet: VPC の IP アドレスの範囲です
  - コンボーネット:VPCルータ、NATゲートウェイ 
  - subnet間通信可能,変更不可 
  - プライベートサブネット
  - パブリックサブネット ルートテーブルで制御,インターネットゲートウェイへのルー
* NAT インスタンス
  * プライペートsubnetからインタネットやリージョンサービス接続用
  * 送信元'/送信先チェックを無効化する必要がある
* ACL
  * sgとの違い:ACLはsubnet単位の設定 と ステートフル/ステートレス
  * セキュリティグループとネットワークACLの違い
    - セキュリティグループ
      - 許可のルールのみを設定できる
      - インスタンスレベルで設定できる
      - ステートフル
        - インバウンド、アウトバウンド両方の通信の設定が必要
      - デフォルトの設定
        - セキュリティグループ外からのインバウンドの通信は全て拒否
        - 同一セキュリティグループ内からのアウトバウンドの通信は全て許可
        - 同一セキュリティグループ内での双方向の通信は全て許可
    - ネットワークACL
      - サブネットレベルで設定できる
      - 許可と拒否のルールが設定できる
      - ステートレス
        - 通信したい場合、インバウンド、アウトバウンド両者の設定をする必要がある
      - デフォルトの設定
        - インバウンド、アウトバウンドの両者が全許可 
* VPCピア接続
  - プライベートip通信
  - 同じリージョン
  - ipアドレス空間の重複はない
  - VPC間接続の手順
    - VPCピア接続を設定
    - ルートテーブルに新規ピア接続を追加(両方のVPCのルートデーブルに追加する必要)
* NAT ゲートウェイ
  * 最大 10 Gbps の帯域幅のバーストをサポート 
* インタネット接続
  - (1)サブネットを作成する
  - (2)インタネットゲートウェイをアタッチ
  - (3)カスタムルートテーブルを作成
  - (4)セキュリティグループルールを更新
  - (5)Elastic ipアドレスを追加
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/20180418-aws-black-belt-online-seminar-amazon-vpc)
* 手順
  * (1)vpc設定
  * (2)vpc から azゾン別にサブネットを設定
  * (3)インタネットゲートウェイを設定しvpcをアタッチ
  * (4)カスタムルートテーブルを作成
  * (5)ピア接続で異なるvpcを繋げる
  * (6)セキュリティグループルールを更新,互い通信できるように設定

ec2 <=> sg <=> subnet <=> Network ACL <=> インタネットゲートウェイ

EC2 Elastic IP <=> get way <=> vpc
vpc -> root table -> root -> 0.0.0.0/0
インタネットに接続するためにElastic IPまたはハイブリックアドレスを設定する必要がある
インスタンス内からid表示 http://169.254.169.254/latest/meta-data/instane-id/


* メインルートテーブルをローカルルートが 1 つしか含まれない元のデフォルトの状態のままにし
* 各サブネットを作成したカスタムルートテーブルの 1 つに明示的に関連付ける

## トラブルシューティング
(1)ec2 システムステータスとインスタンスステータスの両方のチェックに合格すること
(2)sg で必要なポートの接続が許可されている
(3)ネットワーク ACL で必要なポートを経由するトラフィックを許可している
(4)VPC にインターネットゲートウェイがアタッチされていること
(5)ルートテーブルでインターネットゲートウェイを経由するターゲット 0.0.0.0/0 への適切なルートエントリがあるこ
(6)サブネットルートテーブルでインターネットゲートウェイへのルートエントリがあること
(7)パブリック IP アドレスがインスタンスに割り当てられているか、Elastic IP アドレスがインスタンスの Elastic Network Interface (ENI) にアタッチされていること
(8)インスタンスにインストールされているすべての OSレベルのソフトウェアやファイアウォールで必要なポートを経由するトラフィックを許可していること
(9)OS レベルのルートテーブルでインターネットからのトラフィックを許可していることを確認する netstat -rn

# Auto Scaling
* AWS上にdeployされた仮想サーバー群を条件によって、スケールさせるサービス。負荷が大きい時にはサーバー台数を多く、少ない時はサーバー台数を少なくする
* auto scaling policy
* auto scaling launch configuration(auto scalingで扱うインスタンスの種類)
* auto scaling group(auto scalingの台数)
* scalling plan
* スケールアップ、スケールダウン、スケールイン
* クールダウンの設定
  * Auto Scaling グループにおける構成可能な設定で、以前の規模の拡大や縮小が適用される前に、Auto Scaling が追加のインスタンスを起動または終了しないようにします 
* auto scalingのインスタンスはstoppedのステータスを持たない
* auto scalingの利用自体は無料であること

## EBS(Elastic Block Store)
* ランダムアクセスと長期永続性
* RAID5 RAID6が推奨される?
* EC2にアタッチ用ストレージ
* Snapshotを取得しs3に保存可能
  * 静止点を考慮
  * ディスクIOを止めてから撮ると、その時点のsnapshotをとることができる
  * snapshotの取得は非同期に行われるので、取得の終了を待つ必要がない
  * 二世帯以降は増分バックアップ
* 99.999%
* 1G ~ 16T
* 内部的冗長化
* gp2(汎用SSD),io1(高性能SSD),st1(Throughput Optimized HDD),sc1(Cold HDD),Magnetic
* インスタンスストアとの区別
  * 一時的利用と永続化データ
* EBS最適化を有効するEC2が望ましい
* パフォーマンス律速
  * EC2のスループット
  * EBSのIOPS
  * EBSのスループット
* 暗号化
  * 作成時に行う
  * 既存のEBSボリュームの暗号化はできない
* EC2は削除されでも保持可能(EC2と独立)
* 任意のAZゾーンに作成可能
* EBS専用帯域
* ワークロードに向けてストレージパフォーマンス
* コストを最適化オプション
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-amazon-elastic-block-store-ebs)

|                   |  可用性 | スループット | レイテンシ | 
| ----------------- |:-------:|:------------:|:----------:|
| スケールアップ    |    x    |       ●      |     x      |
| Multi Az          |    ●    |       x      |     x      |   
| リードレプリカ    |    x    |       ●      |     x      |
| プロビジョンドIOPS|    x    |       ●      |     ●      |


## ストレージサービスの比較
EBS(ブロック), EFS(ファイル), S3/Glacier(オブジェクト), Storage Gatway(移行)
* EBS
  * gp2, io1(10000 IOPS以上), st1, sc1
  * gp2 => GBあたり3IOPS(base) => ボリュームサイズを大きくすることで得られる性能も向上
  * バーストパフォーマンス


## EFS
* インスタンス間共有でき、プラットフォームを用意拡張
* 自動で容量を拡張/縮小
* 複数AZからアクセス可能
* EBS s3より高い
* EC2と組み合わせて使用
* vs S3
  * file system vs object storage
* vs EBS
  * single EC2 vs thousands of clients
  * low-latency block storage


## s3(Simple Storage Service)
* スタンダード 99.999999999%
* 低冗長化 RRS 99.99%
* 結果整合性
  * 新規は完了が出たら整合
  * 新しいオブジェクトのputは書き込み後の結果整合性
  * それ以外は結果的整合
* アクセス権限と方法
  * ACL
  * パケットポリシ
  * IAM
  * ルールの強さ IAMポリシー > バケットポリシー > ACLポリシー
  * 署名付きURL
* 暗号化
  * クライアント側の暗号化
  * サーバーサイド(S3側)での暗号化
    * SSE-S3
    * SSE-KMS
    * SSE-C: ユーザーが提供した鍵を利用
  * ユーザー責任
* 静的webホスティング
  * EC2より安い
* バージョンニング機能
* ライフサイクル管理
  * 毎日0:00 UTCに処理がキューイングされた順に実行
  * s3(standard) => s3(standard-ia) =>Glacier
* オブジェクト最大5TB
* suport for ipv6
* パフォマンス最適化
  * 100rps超える場合、キー先頭部分の文字列をランダムが推薦
  * GETが多い場合CloudFrontと併用
  * Amazon S3 Transfer Acceleration
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-2017-amazon-s3)

## Glacier
* 安全で耐久性に優れた長期的なオブジェクトストレージ
* 低価格長期バックアップ
* 最も低コストのAWSオブジェクトストレージ(0.005USD/GB vs 0.025USD/GB)
* 1年後Glacierに移動5年後削除など設定可能
* 高速取り出し 1~5分で返される
* S3との違い
  * s3と比べて安い。ただし取り出すの時間がかかる
* 月の無料枠(保存している全体データの5%を日毎に按分)
* S3のライフサイクルと合わせてアーカイブに使える

## 比較

|                   |     料金    |   SLA  | レイテンシ  | 
| ----------------- |:-----------:|:------:|:-----------:|
| S3 標準           | 0.025USD/GB | 99.99% |  ミリ秒     |
| S3 標準 – IA      | 0.019USD/GB | 99.9%  |  ミリ秒     |
| S3 １ ゾーン – IA | 0.0152USD/GB| 99.5%  |  ミリ秒     |
| Glacier           | 0.005USD/GB |該当なし| 分または時  |

# データベース
* 指定できる最大16TB

## RDS (Relational Database Service)
* スタンバイはマルチAz
* EC2上で自前構築との違い
  * 自由度と運用管理の容易性とのトレンドオフ 
* データベースエンジン
  * MySQL
  * MariaDB
  * PostgreSQL
  * Oracle
  * SQL Server
  * Aurora
* リードレプリカ(RR)
  * 5台まで,Auroraは15
  * マルチAzと組み合わせ
  * マスターに昇格
  * ディスクとインスタンスタイプはソースと別に設定可
* バックアップ
  * 優先バックアップウィンドウ
  * バックアップ保持期間
  * 保持期間が変更された場合、即時停止が発生する
  * 0 からゼロ以外の値には、最初のバックアップがすぐに発生するか
  * ゼロ以外の値を 0 にすると、自動バックアップがオフになり、インスタンスの既存の自動バックアップがすべて削除されます。
* スナップショット
  * シングル AZ は短い I/O 停止が発生 
* プライマリーセカンダリーの扱い
* リードレプリカの同期タイミング
  * 非同期で行われる 
* 5分前の状態が復元できる
* 自動スケーリングはない
* 複雑なクエリとテーブルの結合が得意(DynamoDBと比べ)
* DB パラメータグループを変更した場合、変更を反映するのは再起動が必要
* Bring Your Own Licnese ライセンス込み
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/black-belt-online-seminar-aws-amazon-rds)

![スクリーンショット 2018-05-29 13.05.02.png](quiver-image-url/9FF8E1F7918D90256DD416A8FB678AB3.png =1280x800)

## DynamoDB
* Amazon DynamoDB は、整合性があり、10 ミリ秒未満のレイテンシーを必要とする、すべての規模のアプリケーションに対応した、高速かつフレキシブルな NoSQL データベースサービス
* スループットを出すためのキャパシティユニット数の計算方法
  - 読み込み時 4KB * 1秒 につき1キャパシティユニット
  - 強い整合性を持つ読み込みの場合は2KB * 1秒 につき1キャパシティユニット
  - 書き込み 1KB * 1秒 につき1キャパシティユニット
* レンジインデックスとハッシュインデックスについて
* ハイスケーラブル,低レイテンシ,高可用(3x republic)
* 管理不要で高信頼
  - データを3箇所のAzに保存 
* プロビジョンドスループット
  - テーブルごとにReadとWriteそれぞれに対し必要な分だけのスループットキャパシティを割当て
* ストレージ容量制限なし
* 整合性モデル
  - write
    * 二つのAzの保存成功した時点で成功
  - read
    * デフォルトは結果整合性
    * Consistent Readオブション付けクエリ Capacity Unit２倍
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/20150805-aws-blackbeltdynamodb)

## Aurora
* Amazonがクラウド向けにMySQL5.6をベースに作り直したデータベースエンジン
* MySQLのエコシステムそのまま活用可能
* キャッシュ層とデータ層は分離
* DBがクラスター構成で作られる
  - 自動で3つのAZにインスタンスが作成される
  - Writer、Readerの仕組みについて
* 自己修復機能
  - 2つのコピが障害しでも読み書き影響なし
  - 3つのコピが障害しでも読み込み可能
* RDS for mysqlとの違い
  - コスト削減
  - biglogによるレプリケーションではない
  - page cache update
  - フェイルオーバでデータロスなし
* フェイルオーバー時の挙動
  - リードリブレカが存在する場合1分、しない15分
* ストレージが10GBから64TBまでシームレスに拡張できる
* 99.99%の可用性

* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-amazon-aurora)

## ElastiCache
* クラウドでのメモリ内データストアまたはキャッシュのデプロイ、運用、および縮小/拡張を容易にするウェブサービス
* 新しいバージョンのエンジンにはアップグレードできますがダウングレードできません
* RDBMSのクエリ結果をインメモリにキャッシュしてDBの負荷を下げることができる
* Multi AZ - Multi AZ
* フェイルオーバー時の挙動
* RedisとMemchachedの二週類を選んで使うことができる
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-2017-amazon-elasticache-84060910)

Claster
* Redis (クラスターモードが無効) Master-slave クラスター。プライマリエンドポイントをすべての書き込みオペレーションに使用します。個々のノードエンドポイント (API/CLI ではリードエンドポイント) を読み取りオペレーションに使用します。
* Redis (クラスターモードが有効) クラスター。クラスターの設定エンドポイントをすべてのオペレーションに使用します。Redis クラスター (Redis 3.2) をサポートするクライアントを使用する必要があります。個々のノードエンドポイント (API/CLI ではリードエンドポイント) から読み取ることもできます。

## Redshift

* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-amazon-redshift)


# 監視と通知
## CloudWatch
* 基本モニタリング
  * ハイパーバイザーメトリクス無料,間隔は5分
* 詳細モニタリング 間隔は1分単位 
* カスタムメトリックス
  * 間隔は1分単位 
* マネージトサービスはデフォルト,EC2などはハイパーバイザのみ
* アラームとアクション
  * SNS通知, Auto Scaling, EC2アクション 
* OK Alarm INSUFFICIENT 
* CloudWatch Alerm
  * AlermをトリガーにAuto scalingを縮退することができる。
  * Alermをトリガーに他のサービスの起動が可能
* デフォルトのログの保持期間は15か月
* サードパッティツール
* CloudWatch Logs
  * RDS拡張モニタリング
  * Elastica Searchと連携 Kibanaが組み込まれており
  * Subscription => Kinesisに転送
  * https://dev.classmethod.jp/cloud/aws/awslogs-amazonlinux2/
  * /etc/awslogs/awscli.conf
* 料金
  * 5Gまで 10アラーム,10メトリクス 100万API requestまで無料 
* CloudWatch Events
  * イベントソース=> ターゲット => ルール 
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/black-belt-online-seminar-amazon-cloudwatch)
* 監視間隔 http://docs.aws.amazon.com/AmazonCloudWatch/latest/DeveloperGuide/CW_Support_For_AWS.html
* ELB https://dev.classmethod.jp/cloud/aws/elb-and-cloudwatch-metrics-in-depth/

# 拡張性と分散/並列処理
## ELB (Elastic Load Balancing) 
* ALBとCLBに分割
  * CLB (L4+L7) => ALB(L7)
* 抽象化されたロードバランサーサービス
* 複数AZ負荷分散
* EC2,ELB,RDSをMulti AZ配置すると可用性が高いMulti-Datacenter pattern
* ヘルスチェック
* 自動スケーリング
* 負荷が多くなることがある場合はPre-warming(暖機運転) 申請ができること
* Tips
  * 独自ドメインで利用
  * クライアントip取得: X-Forworded-for:
  * VPC利用 subnetをAzごとに設定
* SSLオフロード
* スティキーセッション(stickness)
  * 同じユーザーから来たリクエストは同じec2に送信
  * デフォルトは無効
  * ELB独自セッションCookie
* 自動スケール 
* 急増する場合はPre-Warming(サポートに要請)
* Connection Draining
  * EC2が登録解除したらヘルスチェックが失敗したとき、新規割り振りを中止し、処理中のリクエストが終わるまで一定期間待つ
  * デフォルト有効 timeout 300s
  * timeout最大3600s
* アクセスログをs3に保存
* CloudWatch監視
* Route 53 DNSフェイルオーバ対応
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-2016-elastic-load-balancing)

## Auto Scaling
* EC2 => CloudWatch =>アラーム => AutoScaling
* (1)起動設定
* (2)Auto Scaling Group
* (3)Auto Scalingポリシー
  * Step scaling vs simple scaling
* 2大原則(デフォルト設定)
  * Desired Capacityを維持するためにヘルスチェック
  * AZ間均等

## SQS(simple queue service)
* Amazon Simple Queue Service (SQS) は、完全マネージド型のメッセージキューイングサービス
* Pull型
* 順序性の保証はしない
* 最低一回配信保証(明示的な削除が必要)
* 可視性time-out(別のサーバーが処理中と分かる)
* メッセージサイズ最大256kb
* リージョンサービス
* ロングポーリングについて
  * ロングポーリングを行った方が良いケースについて
* SNSと連動した利用方法について
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-tech-amazon-sqs-amazon-sns)

## SNS (Amazon Simple Notification Service)
* Amazon Simple Notification Service (SNS) は柔軟なフルマネージド型の pub/sub メッセージング/モバイル通知サービス
* 対応しているプロトコル
  * SMS
  * HTTP/HTTPS
  * Email/Email-JSON
  * Mobile push
  * SQS
* サブスクライバー
* トピック
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-tech-amazon-sqs-amazon-sns)

## SWF(Amazon Simple Workflow Service)
* 処理のステート管理とタスク間のコーディネートを行うためのフルマネージドサービス
* Workflow starter
* ディサイダー
* アクティビティワーカー
* 厳密に一回限りで順序性を求める処理に適している
* [入門](http://dev.classmethod.jp/cloud/aws/cm-advent-calendar-2015-getting-started-again-swf/)

# DNSとCDN
## Route 53
* 可用性が高くスケーラブルなクラウドドメインネームシステム (DNS) ウェブサービス
* エッジロケーション
  * DNS CDNサーバー
* A CNAME ALIAS => Zone Apex
* ヘルスチェック/フェイルオーバー
  * HTTP, HTTPS, SSH 
* レイテンシーベースルーティン
* サポートタイプ
  * A(アドレス)
  * AAAA(IPv6)
  * CNAME
  * MX
  * NS
  * PTR
  * SOA
  * SPF
  * SRV
  * TXT
* ルーティングポリシー
  * シンプルルーティングポリシー – ドメインで特定の機能を実行する単一のリソースがある場合に使用します
  * フェイルオーバールーティングポリシー
  * 位置情報ルーティングポリシー
  * レイテンシールーティングポリシ
  * 複数値回答ルーティングポリシー – アトランダムに選ばれた最大 8 つの正常なレコードを持つ DNS クエリに Amazon Route 53 を応答させる場合に使用します
  * 加重ルーティングポリシー – 指定した比率で複数のリソースにトラフィックをルーティングするのに使用します
* Aliasレコードでzone apexを解決する
* Elastic IP => Route53
* 検証 nslookup => どメーン => ipが正しいか?

```
Address:	192.168.30.1#53

Non-authoritative answer:
www.clouddesignpattern.org	canonical name = www.clouddesignpattern.org.s3-website-us-west-1.amazonaws.com.
www.clouddesignpattern.org.s3-website-us-west-1.amazonaws.com	canonical name = s3-website-us-west-1.amazonaws.com.
Name:	s3-website-us-west-1.amazonaws.com
Address: 54.231.235.30 #Aレコードに設定したipアドレス
```

## CloudFront
* Amazon CloudFront は低レイテンシーの高速転送によりデータ、ビデオ、アプリケーション、APIをビューワーに安全に配信するグローバルコンテンツ配信ネットワーク (CDN) サービス
* エッジロケーション、アクセスされた場所から近いキャッシュサーバー
* Create Distribution => キャッシュの制御
* GET/ HEAD / OPTION (Cache)
* PUT/ POST/ DELETE/ PATCH (Proxy)
* 動的コンテンツキャッシュ
  * Header,Cookie,Query String情報をフォワード
* Behaviors Cache(正規表現)
* カスタムエラーページ
* セキュア配信
  * デフォルト証明書、独自SSL証明書、SNI独自SSL
  * オリジン暗号化通信
* オリジンカスタムヘッダー
* GEO Restriction
* 名付きURL/Cookie
* AWS WAF連携
* ストリーミング配信
* Lambdaと連携して自動キャッシュ無効化
* [入門](https://dev.classmethod.jp/cloud/cm-advent-calendar-2015-aws-re-entering-cloudfront/)
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-tech-2016-amazon-cloudfront)
 

# 構成管理
## KMS(AWS Key Management Service)
* AWS Key Management Service (KMS) とは、データの暗号化に使用する暗号化キーを簡単に作成および管理できるマネージド型サービス
* EC2,S3,RDSなどの各種AWSと連携して暗号化の鍵として使うことができる
* 暗号鍵は99.999999999%の耐久性
* キーの操作履歴をCloudTrailログに残し、監査可能
* [ドキュメント](https://aws.amazon.com/jp/kms/)
* [入門](https://dev.classmethod.jp/cloud/aws/cm-advent-calendar-2015-aws-relearning-key-management-service/)

## Trusted Advisor
* ベストプラクティスに沿ってリソースをプロビジョニングすることにより、システムのパフォーマンスや信頼性を向上させ、セキュリティを高め、コスト削減の可能性を探せるサービス
* コンソールパネルから利用者のaws環境を「コスト最適化」、「パフォーマンス」、「セキュリティ」、「フォールトトレーランス」の観点から分析し、推奨設定を教えてくれる
* [入門](https://dev.classmethod.jp/cloud/aws/cm-advent-calendar-2015-getting-started-again-aws-td/)

## CloudTrail
* AWS CloudTrail は、AWS アカウントのガバナンス、コンプライアンス、運用監査、リスク監査を可能にするサービス
* 各APIの呼び出しを記録して、ログをS3に保存することができる
* [ドキュメント](https://aws.amazon.com/jp/cloudtrail/)


## AWS Config
* レポジトリ情報からAWSリソースの変更履歴、構成変更を管理するサービス
* AWS Configに対応しているサービス
  - EC2
  - VPC
  - EBS
  - IAM
  - RDS
  - CloudTrail
  - Certificate Manager
* [ドキュメント](https://aws.amazon.com/jp/config/faq/)

## AWS Import/Export
* 安全な物理的な移動を目的として設計されたストレージアプライアンスを使用して、または AWS への移動をスピードアップさせるためのデータ転送ソリューション
* 数百テラを超える場合はこれを使う
* 大量データテラからペタバイト、最近はsnowmobileというのが出てヘクサバイトまで対応し始めた

## AWS Storage Gateway
* WS Storage Gateway は、オンプレミスアプリケーションが AWS クラウドでのストレージをシームレスに使用できるようにする、ハイブリッドストレージサービス
* オンプレミスの環境とクラウドの環境を接続して、段階的な移行を実現する
* S3やGlacierとの連携を行うことができる
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-2017-aws-storage-gateway)


## AWS Direct Connect
* AWS環境から顧客環境に専用線をひくサービス
* データ移行や大量の帯域が必要な顧客の場合に使う
* クロスアカウント利用
* 月額利用料
  * ポート利用料
  * データ転送量
* オンプレミスとの連携設定など
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-tech-aws-direct-connect)

# デプロイ
## CloudFormation
* AWS CloudFormation は、開発や本運用に必要な、互いに関連する AWS リソースのコレクションを作成しておき、そのリソースを適切な順序でプロビジョニングするためのサービス
* 構造図からリソース自動作成
* 記述形式テンプレート
  * JSON, YAML 
* Resources
* スタック
* Template
* Parameter
* Conditions
* Outputs
* vs Elastic Beanstalk
* vs OpsWorks
* [ドキュメント](https://aws.amazon.com/jp/cloudformation/faqs/)
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-2016-aws-cloudformation)


## Elastic Beanstalk
* 環境構築やデプロイをこなす
* 利用できる言語・platformについて
  * Java SE,Java(Tomcat),PHP,Ruby,Python,Node.js,.NET,docker
* 自動的にデプロイされるサービスについて
  * ELB,EC2,RDS,Auto Scaling,S3,SNS
* アプリケーション, 環境, バージョン
* ウェブサーバー環境
  * ELB + Auto Scaling
* ワーカー環境
  * SQS + Auto Scaling 
* [black-belt](https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-2017-aws-elastic-beanstalk)
```
eb config

```


## OpsWorks
* Chefを利用するサービスであることについて
* デプロイでの環境構築を自動化する
* [ドキュメント](https://aws.amazon.com/jp/opsworks/chefautomate/faqs/)


## aws cli
aws configure

## SNS
Create Topic


## dns
ec2-public-ipv4-address.region.amazonaws.com 
ec2-13-112-188-237.ap-northeast-1.computer.amazonaws.com

## Lambda

## Edge
A/Bテストなど活用
オリージの前に軽い処理
URL書き替え A/Bテスト　静的ページ　従量課金
  
request <=> CloudFront <=> Lambda Edge <=> origin


## Amazon Container Services
 * レジストリ
   * Amazon Elastic Container Registry(ECR)
 * コントロールブレーン 
   * コンテナを管理する場所
   * ECS / EKS
 * データプレーン 
   * 実際稼働する場所
   * Fargate / EC2

# その他
* 契約
  * awsはいつもで利用契約を変更できる
  * データが漏洩したらユーザーの責任
  * 契約の基本は英語、裁判ば米国
  * サービス撤退は30日前にユーザーに公知
* タグ
  * 全てのリソースは付けられる 
* Ec2 config service
  * EBSボリュームをフォーマット 

## セキュリティ
* AWSを利用する上でのセキュリティ及び、コンプライアンスに関しての理解
  * AWSの共有責任モデルを理解する
* 侵入テスト
  * 侵入テストを行う時は自分の環境でもAWSへの事前連絡が必要
* [AWSのセキュリティのベストプラクティス](https://d0.awsstatic.com/International/ja_JP/Whitepapers/AWS_Security_Best_Practices.pdf) 

# code兄弟
https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-tech-2015-aws-codecommit-aws-codepipeline-aws-codedeploy?qid=6f158000-6047-40a1-8a66-60a52d05e186&v=&b=&from_search=1

https://www.slideshare.net/AmazonWebServicesJapan/aws-black-belt-online-seminar-aws-code-services-part-2?qid=6f158000-6047-40a1-8a66-60a52d05e186&v=&b=&from_search=4
## CodeCommit
* リポジトリ作成
* IAM -> ユーザー(自分のアカウント) -> 認証情報 -> SSH公開キーアプロード  
インスタンスに対するアクセス権限を AWS CodeDeploy に付与するサービスロールを選択します。

## CodeBuild
* k8s CICD: https://dev.classmethod.jp/cloud/aws/aws-auth-configmap-cd/ 


## CodeDeploy
EC2にインスール
デプロイメントグループで環境指定

$ sudo yum update
$ sudo yum install ruby
$ sudo yum install aws-cli
$ cd /home/ec2-user
$ aws s3 cp s3://aws-codedeploy-ap-northeast-1/latest/install . --region ap-northeast-1
$ wget https://aws-codedeploy-ap-northeast-1.s3.amazonaws.com/latest/install
$ chmod +x ./install
$ sudo ./install auto

インストールの確認

$ sudo service codedeploy-agent status

appspec.yml
| 項目                    | 	説明                                                                 	| 例               |
| ----------------------- | :----------------------------------------------------------------------:| ---------------- |
| version	                | デプロイバージョン                                                      |	                 |
| os                      |	デプロイ先のEC2インスタンスのos                                         |	linux or windows |
| files:source          	| デプロイ元のソースのパス。全ソースをデプロイする場合は「/」	            | /                |
| files:destination      	| ソースを配置する、デプロイ先のEC2インスタンスのパス	                    | /home/release    |
| hooks :ApplicationStart	| デプロイ実行前に行いたい処理がある場合に、シェルのファイル名等設定する	| location: scripts/codedeploy_start.sh |

https://github.com/aws-samples/aws-codedeploy-samples/tree/master/conf-mgmt/ansible/local-only

先搜尋這些檔案是否曾經 deploy 過，如果是，就刪除本地檔案，上傳新的檔案
若為否則不會刪除本地檔案，所以在 deploy 的時候就會出現「File already exists at location」的錯誤

ApplicationStop
BeforeInstall
AfterInstall
ApplicationStart
ValidateService

http://dev.classmethod.jp/cloud/aws/code-deploy-appspec/

デプロイグループに設定するIAM
AWSCodeDeployRoleをアタッチ
信頼関係
```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "",
      "Effect": "Allow",
      "Principal": {
        "Service": [
          "codedeploy.amazonaws.com"
        ]
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```

EC2に設定するIAM
AmazonEC2RoleforAWSCodeDeployをアタッチ
信頼関係
```
{
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "Service": "ec2.amazonaws.com"
        },
        "Action": "sts:AssumeRole"
      }
    ]
  }
```
Bitbucket Role
```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": "arn:aws:iam::984525101146:root"
      },
      "Action": "sts:AssumeRole",
      "Condition": {
        "StringEquals": {
          "sts:ExternalId": "connection:2958365"
        }
      }
    }
  ]
}
```

/var/log/aws/codedeploy-agent/* 
/opt/codedeploy-agent/deployment-root/deployment-logs/* 

https://aws.amazon.com/jp/blogs/devops/automatically-deploy-from-github-using-aws-codedeploy/


```hooks/before_install.sh
#!/usr/bin/env bash
 
app_src_path="/usr/src/app"
app_log_path="/var/log/app"
 
mkdir "$app_src_path" "$app_log_path"
```

```hooks/application_start.sh
#!/usr/bin/env bash
 
app_path="/usr/src/app/bin/www"
forever_log="/var/log/app/forever.log"
app_access_log="/var/log/app/access.log"
app_error_log="/var/log/app/error.log"
 
forever start \
  -l "$forever_log" \
  -o "$app_access_log" \
  -e "$app_error_log" \
  "$app_path"
```

```
#!/bin/bash
result=$(curl -s http://localhost:8888/hello/)

if [[ "$result" =~ "Hello World" ]]; then
    exit 0
else
    exit 1
fi
```
```
URL = http://hogehoge.com"
HTTP_RESPONSE=$(curl --silent --write-out "HTTPSTATUS:%{http_code}" -X GET $URL)
HTTP_STATUS=$(echo $HTTP_RESPONSE | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')
echo "$HTTP_STATUS"
if [ "$HTTP_STATUS" -eq '200' ]; then
    exit 0"
else
    exit 1
fi
```
```
netstat -ntlp | grep 6443 || exit 1
```


フックの環境変数
```
if [ "$DEPLOYMENT_GROUP_NAME" == "Staging" ]
then
    sed -i -e 's/Listen 80/Listen 9090/g' /etc/httpd/conf/httpd.conf
fi
```

* debug

```
cd /var/log/aws/codedeploy-agent
tail -f codedeploy-agent.log
cat /var/log/aws/codedeploy-agent/codedeploy-agent.log
/opt/codedeploy-agent/deployment-root/deployment-logs/codedeploy-agent-deployments.log
```
bundle install --path vendor/bundle/

## CodePipeline
CodeCommit CodeBuild CodeDeployを繋ぐPipeline

作成 => 名前入力 => ソース所在を選ぶ(Githut S3 CodeCommit) => ビルド作成(CodeBuild Jenkins SolanoCIのどちを選ぶ) => デプロイ方法を選ぶ(CodeDeploy CloudFormation ElasticBeanstalk)
=> roleを選ぶ 



```
2017-06-29T08:22:39 ERROR [codedeploy-agent(2714)]: InstanceAgent::Plugins::CodeDeployPlugin::CommandPoller: Error during perform: InstanceAgent::Plugins::CodeDeployPlugin::ScriptError - Script does not exist at specified location: /opt/codedeploy-agent/deployment-root/b96beb04-014f-40bd-a12e-59f4cf02de19/d-OT0B5L4GM/deployment-archive/script/application_start.sh - /opt/codedeploy-agent/lib/instance_agent/plugins/codedeploy/hook_executor.rb:105:in `block (2 levels) in execute'
```

```
 > /dev/null 2> /dev/null < /dev/null &
 
 sudo service awslogs stop
 ```

