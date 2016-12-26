#!/usr/bin/env perl
use 5.014;
use MongoDB;
use utf8;
use open qw/:encoding(UTF-8) :std/;
use Test::Simple tests => 7;

my $dbConnectionString;

if ($ARGV[0] eq "dbconnect"){
	(undef, $dbConnectionString) = (shift, shift);
}

my $dbclient = MongoDB->connect($dbConnectionString);


my $collection = $dbclient->ns( 'QuotesDB.quotes' );
my @records = (["Быть или не быть - вот в чем вопрос", "У. Шекспир"],
["Veni, Vedi, Veci", "A. Makedonskiy"],
["Жизнь - это очень короткое время между двумя 456 вечностями", "Карлейль Т."]);#3 quotes



ok $collection->count() > 0, "Database and collection exist";
ok $collection->count() >= 3, "Collection contains greater then or equal 3 records";
ok $collection->count({quotestext => {'$exists' => 1}}) > 0, "Documents have field 'quotestext'";
ok $collection->count({author => {'$exists' => 1}}) > 0, "Documents have field 'author'";
for (@records){
  ok $collection->count({quotestext => @$_[0], author => @$_[1]}) > 0, "Exists quote '@$_[0]' by '@$_[1]'";
}
