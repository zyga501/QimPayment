//
//  nextZhanghuViewController.m
//  The territory of payment
//
//  Created by 铂金数据 on 15/4/1.
//  Copyright (c) 2015年 铂金数据. All rights reserved.
//

#import "nextZhanghuViewController.h"
#import "Header.h"
#import "MyNavigationBar.h"
#import "User.h"
#import "PostAsynClass.h"
@interface nextZhanghuViewController ()
{
    UIView *_image;
    
    UILabel *_nameLab;
    
    UILabel *_phoneLab;
    
    UILabel *_xiLab;
    
    UILabel *_zhuLab;
    
    UILabel *_time;
    
    UILabel *_body;
    
    UILabel *_shiMing;
    
    UILabel *_t0;
    
    UILabel *_t1;
    
    UILabel *_up;
    
    UILabel *_down;
    
    UILabel *_shangci;
    
    UILabel *_jiejiKa1;
    
    UILabel *_jiejiKa2;
    
    UILabel *_daijiKa;
    
    UIScrollView *_sv;
    
}
@end

@implementation nextZhanghuViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self makeNav];
    [self makeUI];
    self.view.backgroundColor=[UIColor whiteColor];    // Do any additional setup after loading the view.
}
-(void)makeNav
{
    
    
    _sv=[[UIScrollView alloc] initWithFrame:CGRectMake(0,44, screenWidth, screenHeight)];
    _sv.backgroundColor=[UIColor whiteColor];
    _sv.contentSize=CGSizeMake(0, 408+44);
    _sv.delegate=self;
    _sv.showsVerticalScrollIndicator = NO;
    _sv.bounces=NO;
    [self.view addSubview:_sv];
    
    
    
    //导航
    [self.navigationController setNavigationBarHidden:YES];
    MyNavigationBar *mnb = [[MyNavigationBar alloc] init];
    mnb.frame = CGRectMake(0, 20*screenWidth/320, 320*screenWidth/320, 44*screenWidth/320);
    [mnb createMyNavigationBarWithBgImageName:nil andLeftBBIIamgeNames:[NSArray arrayWithObject:@"title_back.png"] andRightBBIImages:nil andTitle:@"账户详情" andClass:self andSEL:@selector(bacClick:)];
    [self.view addSubview:mnb];
    
    //状态栏颜色
    UIView *statusBarView=[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320*screenWidth/320, 20*screenWidth/320)];
    
    statusBarView.backgroundColor=[UIColor colorWithRed:0/255.0 green:55/255.0 blue:113/255.0 alpha:1];
    [self.view addSubview:statusBarView];
    
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:NO];

    
    
}
-(void)bacClick:(UIButton*)btn
{
    [self.navigationController popViewControllerAnimated:YES];
}

-(void)makeUI
{
    //文字
    NSArray *titArr=@[@"姓名",@"手机号",@"系统编号",@"注册日期",@"上次登录时间",@"身份证号",@"绑定收款银行卡状态",@"提现手续费",@"提现手续费"];
     //灰色横条
    for(int i=0;i<titArr.count;i++)
    {
        _image =[[UIImageView alloc] initWithFrame:CGRectMake(0, (40+i*41)*screenWidth/320, self.view.frame.size.width, 1*screenWidth/320)];
        _image.backgroundColor=[UIColor lightGrayColor];
        [_sv addSubview:_image];
    }
  
   
    for(int i=0;i<titArr.count;i++)
    {
        UILabel *lab=[[UILabel alloc] initWithFrame:CGRectMake(15*screenWidth/320, (7+i*40)*screenWidth/320, 150*screenWidth/320, 30*screenWidth/320)];
        
        lab.text=titArr[i];
        lab.font=[UIFont systemFontOfSize:15];
        
        [_sv addSubview:lab];
    }
    NSArray *littleArr=@[@"(5万以下)",@"(5万以上)"];
    for(int i=0;i<littleArr.count;i++)
    {
        UILabel *littleLab=[[UILabel alloc] initWithFrame:CGRectMake(15*screenWidth/320, (315+i*40)*screenWidth/320, 100*screenWidth/320, 10*screenWidth/320)];
        littleLab.text=littleArr[i];
        littleLab.font=[UIFont systemFontOfSize:14];
        [_sv addSubview:littleLab];
    }
    
    
    //% 元/笔
    NSArray *arr=@[@"元/笔",@"元/笔"];
    for(int i=0;i<arr.count;i++)
    {
        UILabel *lla=[[UILabel alloc] initWithFrame:CGRectMake(260*screenWidth/320, (302+i*40)*screenWidth/320, 40*screenWidth/320, 15*screenWidth/320)];
        lla.text=arr[i];
        lla.textAlignment= NSTextAlignmentRight;
        lla.font=[UIFont systemFontOfSize:13];
        [_sv addSubview:lla];
    }
    User * user = [User currentUser];
    
    NSMutableURLRequest *rever = [PostAsynClass postAsynWithURL:url1 andInterface:url11 andKeyArr:@[@"merId",@"loginId"]andValueArr:@[user.merid,user.phoneText]];
    //
    NSData *receive = [NSURLConnection sendSynchronousRequest:rever returningResponse:nil error:nil];
    
    NSStringEncoding enc = CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000);
    
    
    //NSString *string=[receive stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
    NSString  *str = [[NSString alloc] initWithData:receive encoding:enc];
    
    NSData *ata = [NSData alloc];
    
    ata = [str dataUsingEncoding:NSUTF8StringEncoding];
    
    NSDictionary *dic= [NSJSONSerialization JSONObjectWithData:ata options:NSJSONReadingMutableLeaves error:nil];
    user.one=[dic objectForKey:@"merName"];
    user.two=[dic objectForKey:@"loginId"];
    user.three=[dic objectForKey:@"merId"];
    user.four=[dic objectForKey:@"openDate"];
    
    
    
    user.six=[dic objectForKey:@"certId"];
    user.seven=[dic objectForKey:@"isAuthentication"];
    user.eight=[dic objectForKey:@"feeRateT0"];
    user.nine=[dic objectForKey:@"feeRateT1"];
    
    user.ten=[dic objectForKey:@"feeRateLiq1"];
    user.eleven=[dic objectForKey:@"feeRateLiq2"];
    
    user.jieji1=[dic objectForKey:@"debitFeeRateT0"];
    user.jieji2=[dic objectForKey:@"debitFeeRateT1"];
    NSLog(@"xiangxixinxi%@",dic);
    
    
    
    //可变文字
    //姓名
    _nameLab=[[UILabel alloc] initWithFrame:CGRectMake(150*screenWidth/320, 8*screenWidth/320, 150*screenWidth/320, 20*screenWidth/320)];
    _nameLab.text=user.one;
    _nameLab.textAlignment= NSTextAlignmentRight;
    _nameLab.font=[UIFont systemFontOfSize:14];
    [_sv addSubview:_nameLab];
    
    
    //手机号
    _phoneLab=[[UILabel alloc] initWithFrame:CGRectMake(150*screenWidth/320, 52*screenWidth/320, 150*screenWidth/320, 20*screenWidth/320)];
    _phoneLab.text=user.two;
    _phoneLab.textAlignment= NSTextAlignmentRight;
    _phoneLab.font=[UIFont systemFontOfSize:14];
    [_sv addSubview: _phoneLab];
    
    
    //系统编号
    _xiLab=[[UILabel alloc] initWithFrame:CGRectMake(150*screenWidth/320, 92*screenWidth/320, 150*screenWidth/320, 20*screenWidth/320)];
    _xiLab.text=user.three;
    _xiLab.textAlignment= NSTextAlignmentRight;
    _xiLab.font=[UIFont systemFontOfSize:14];
    [_sv addSubview: _xiLab];
    
    
    
    //注册日期
    _zhuLab=[[UILabel alloc] initWithFrame:CGRectMake(150*screenWidth/320, 132*screenWidth/320, 150*screenWidth/320, 20*screenWidth/320)];
    _zhuLab.text=user.four;
    
    //把20130303变成2013-03-03
    NSRange range1=NSMakeRange(0, 4);
    NSRange range2=NSMakeRange(4, 2);
    NSRange range3=NSMakeRange(6, 2);
    NSString *string1=[NSString stringWithFormat:@"%@-%@-%@",[_zhuLab.text substringWithRange:range1],[_zhuLab.text substringWithRange:range2],[_zhuLab.text substringWithRange:range3]];
    _zhuLab.text=string1;
    NSLog(@"%@",string1);
    
    
    _zhuLab.textAlignment= NSTextAlignmentRight;
    _zhuLab.font=[UIFont systemFontOfSize:14];
    
    
   
    [_sv addSubview: _zhuLab];
    
    if ([user.sm isEqualToString:@"S"]) {
    
    //上次登录时间
    _time=[[UILabel alloc] initWithFrame:CGRectMake(150*screenWidth/320, 172*screenWidth/320, 150*screenWidth/320, 20*screenWidth/320)];
    _time.text=user.date;
    _time.textAlignment= NSTextAlignmentRight;
    _time.font=[UIFont systemFontOfSize:14];
    NSRange ran1=NSMakeRange(0, 4);
    NSRange ran2=NSMakeRange(4, 2);
    NSRange ran3=NSMakeRange(6, 2);
    NSRange ran4=NSMakeRange(8, 2);
    NSRange ran5=NSMakeRange(10, 2);
    NSRange ran6=NSMakeRange(12, 2);
    NSString *string2=[NSString stringWithFormat:@"%@-%@-%@ %@:%@:%@",[_time.text substringWithRange:ran1],[_time.text substringWithRange:ran2],[_time.text substringWithRange:ran3],[_time.text substringWithRange:ran4],[_time.text substringWithRange:ran5],[_time.text substringWithRange:ran6]];
    _time.text=string2;
    [_sv addSubview: _time];
    
    
    //身份证号
    _body=[[UILabel alloc] initWithFrame:CGRectMake(130*screenWidth/320, 218*screenWidth/320, 170*screenWidth/320, 20*screenWidth/320)];
    _body.text=user.six;
        //身份证号中间加密
        
            if(_body.text.length>10)
            {
                _body.text=[_body.text stringByReplacingCharactersInRange:NSMakeRange(_body.text.length-10, 6) withString:@"******"];
            }
//    NSString*before=[_body.text substringToIndex:6];
//    
//    NSString*after=[_body.text substringFromIndex:14];
//    
//    _body.text = [[NSString alloc] initWithFormat:@"%@********%@", before, after];
//        
    _body.textAlignment= NSTextAlignmentRight;
    _body.font=[UIFont systemFontOfSize:14];

    [_sv addSubview: _body];
    
        
    }
    
        
    //实名状态
    _shiMing=[[UILabel alloc] initWithFrame:CGRectMake(150*screenWidth/320, 262*screenWidth/320, 150*screenWidth/320, 20*screenWidth/320)];
    _shiMing.text=user.sm1;
    _shiMing.textAlignment= NSTextAlignmentRight;
    _shiMing.font=[UIFont systemFontOfSize:14];
    [_sv addSubview: _shiMing];
 
    //提现手续费
    _up=[[UILabel alloc]initWithFrame:CGRectMake(195*screenWidth/320, 300*screenWidth/320,70*screenWidth/320, 20*screenWidth/320)];
    _up.text=user.ten;
    _up.textAlignment= NSTextAlignmentRight;
    _up.font=[UIFont systemFontOfSize:14];
    [_sv addSubview:_up];
    
    
    //提现手续费
    _down=[[UILabel alloc]initWithFrame:CGRectMake(195*screenWidth/320, 340*screenWidth/320,70*screenWidth/320, 20*screenWidth/320)];
    _down.text=user.eleven;
    _down.textAlignment= NSTextAlignmentRight;
    _down.font=[UIFont systemFontOfSize:14];
    [_sv addSubview:_down];
   
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    //如果一个界面有多个提示框，通过tag值区分，就像textField一样
    
    //通过buttonIndex来确定点击的是提示框上的哪个按钮
    if (!buttonIndex) {
        
        
    } else {
        
        [self.navigationController popToRootViewControllerAnimated:YES];
        
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
