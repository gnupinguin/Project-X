#!/usr/bin/env perl
use 5.014;
use MongoDB;
use utf8;
use open qw/:encoding(UTF-8) :std/;
use Test::Simple tests => 6;
use HTTP::Request::Common qw(POST); 
use LWP::UserAgent;

my $dbConnectionString;

if ($ARGV[0] eq "dbconnect"){
	(undef, $dbConnectionString) = (shift, shift);
}

my $dbclient = MongoDB->connect($dbConnectionString);

my $ua = LWP::UserAgent->new;

my $req = POST 'http://127.0.0.1:3000/add', [];
$req->content('quote=blabla&author=&form1=');
 
#say $ua->request($req)->as_string;
$req->header('Content-Length' => length 'quote=blabla&author=&form1=');
$req->content('quote=blabla&author=&form1=');
ok ($ua->request($req)->code != 200, "Empty quote field");

$req->header('Content-Length' => length 'quote=blabla&author=&form1=');
$req->content('quote=blabla&author=&form1=');
ok ($ua->request($req)->code != 200, "Empty author field");

$req->header('Content-Length' => length 'quote=&author=&form1=');
$req->content('quote=&author=&form1=');
ok ($ua->request($req)->code != 200, "Both field are empty");

$req->header('Content-Length' => length 'quote=+&author=Author&form1=');
$req->content('quote=+&author=Author&form1=');
ok ($ua->request($req)->code != 200, "Quote field is space");

$req->header('Content-Length' => length 'quote=quote&author=+&form1=');
$req->content('quote=quote&author=+&form1=');

ok ($ua->request($req)->code != 200, "Author field is space");

$req->header('Content-Length' => length 'quote= &author= &form1=');
$req->content('quote= &author= &form1=');
ok ($ua->request($req)->code != 200, "Both fields are space");

my $collection = $dbclient->ns( 'QuotesDB.quotes' );
#$collection->delete_many({"author"=>""});
#$collection->delete_many({"quotestext"=>""});
$collection->delete_many({"author"=>qr/^\s*$/});
$collection->delete_many({"quotestext"=>qr/^\s*$/});

