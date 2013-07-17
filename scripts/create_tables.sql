drop table players;
create table players (
	id int not null primary key,
	name varchar(100) not null,
	position varchar(5) not null
);

drop table stats;
create table stats (
	id int auto_increment primary key,
	player_id int not null,
	season int not null,
	week int not null,
	stat_key int not null,
	stat_value int not null,
	foreign key (player_id) references player(id)
);
create index stat_key_idx on stats(stat_key);
create index stat_season_idx on stats(season);

drop table fantasy_points;
create table fantasy_points (
	id int auto_increment primary key,
	player_id int not null,
	system varchar(100) not null,
	season int not null,
	week int not null,
	points int not null,
	projection boolean default false,
  num_startable int,
  num_owners int
);

drop table teams;
create table teams(
	id int auto_increment primary key,
	city varchar(20) not null,
	name varchar(20) not null,
	abbreviation varchar(3) not null
);
insert into teams (city, name, abbreviation) values ('Atlanta','Falcons','ATL');
insert into teams (city, name, abbreviation) values ('Buffalo','Bills','BUF');
insert into teams (city, name, abbreviation) values ('Chicago','Bears','CHI');
insert into teams (city, name, abbreviation) values ('Cincinnati','Bengals','CIN');
insert into teams (city, name, abbreviation) values ('Cleveland','Browns','CLE');
insert into teams (city, name, abbreviation) values ('Dallas','Cowboys','DAL');
insert into teams (city, name, abbreviation) values ('Denver','Broncos','DEN');
insert into teams (city, name, abbreviation) values ('Detroit','Lions','DET');
insert into teams (city, name, abbreviation) values ('Green Bay','Packers','GB');
insert into teams (city, name, abbreviation) values ('Tennessee','Titans','TEN');
insert into teams (city, name, abbreviation) values ('Indianapolis','Colts','IND');
insert into teams (city, name, abbreviation) values ('Kansas City','Chiefs','KC');
insert into teams (city, name, abbreviation) values ('Oakland','Raiders','OAK');
insert into teams (city, name, abbreviation) values ('St. Louis','Rams','STL');
insert into teams (city, name, abbreviation) values ('Miami','Dolphins','MIA');
insert into teams (city, name, abbreviation) values ('Minnesota','Vikings','MIN');
insert into teams (city, name, abbreviation) values ('New England','Patriots','NE');
insert into teams (city, name, abbreviation) values ('New Orleans','Saints','NO');
insert into teams (city, name, abbreviation) values ('New York','Giants','NYG');
insert into teams (city, name, abbreviation) values ('New York','Jets','NYJ');
insert into teams (city, name, abbreviation) values ('Philadelphia','Eagles','PHI');
insert into teams (city, name, abbreviation) values ('Arizona','Cardinals','ARI');
insert into teams (city, name, abbreviation) values ('Pittsburgh','Steelers','PIT');
insert into teams (city, name, abbreviation) values ('San Diego','Chargers','SD');
insert into teams (city, name, abbreviation) values ('San Francisco','49ers','SF');
insert into teams (city, name, abbreviation) values ('Seattle','Seahawks','SEA');
insert into teams (city, name, abbreviation) values ('Tampa Bay','Buccaneers','TB');
insert into teams (city, name, abbreviation) values ('Washington','Redskins','WAS');
insert into teams (city, name, abbreviation) values ('Carolina','Panthers','CAR');
insert into teams (city, name, abbreviation) values ('Jacksonville','Jaguars','JAC');
insert into teams (city, name, abbreviation) values ('Baltimore','Ravens','BAL');
insert into teams (city, name, abbreviation) values ('Houston','Texans','HOU');

drop table team_memberships;
create table team_memberships (
	id int auto_increment primary key,
	player_id int not null,
	season int not null,
	team_id int not null,
	version int,
	foreign key (player_id) references player(id),
	foreign key (team_id) references teams(id)
);
create index player_season_team_idx on team_memberships(player_id, season, team_id);

drop table average_draft_positions;
create table average_draft_positions (
	id int auto_increment primary key,
	player_id int not null,
	season int not null,
	adp double not null,
	version int,
	foreign key (player_id) references player(id)
);
create index adp_season_idx on average_draft_positions(season);

drop table users;
create table users (
	id int auto_increment primary key,
	username varchar(100) not null,
	password varchar(200) not null,
	enabled boolean,
	account_expired boolean,
	account_locked boolean,
	password_expired boolean,
	version int
);

drop table roles;
create table roles (
	id int auto_increment primary key,
	authority varchar(100) not null,
	version int
);

drop table user_roles;
create table user_roles (
	id int auto_increment primary key,
	user_id int not null,
	role_id int not null,
	version int,
	foreign key (user_id) references users(id),
	foreign key (role_id) references roles(id)
);

/*
 * Represents the various fantasy football service providers that exist.  We
 * initially seed the database with the major ones - ESPN, Yahoo!, CBS, and NFL.com.
 */
drop table fantasy_league_types;
create table fantasy_league_types (
  id int auto_increment primary key,
  code varchar(10) not null,
  description varchar(100) not null,
  version int
);
insert into fantasy_league_types(code, description) values('ESPN', 'ESPN Fantasy Football League');
insert into fantasy_league_types(code, description) values('Yahoo!', 'Yahoo! Fantasy Football League');
insert into fantasy_league_types(code, description) values('CBS', 'CBS Fantasy Football League');
insert into fantasy_league_types(code, description) values('NFL.com', 'NFL.com Fantasy Football League');

/*
 * Model for a single user's fantasy football team.  A user can have multiple, so
 * there would be an entry for their ESPN league and their Yahoo! league.  Each entry
 * only represents a single season, hence the column for season.
 *
 * Lastly, I've added a league_id column to record in case the provider allows for
 * API calls to be made.  This way, we'll be able to do cool things like pull all the
 * league info from the provider's servers.
 */
drop table fantasy_teams;
create table fantasy_teams (
  id int auto_increment primary key,
  user_id int not null,
  league_id varchar(100),
  name varchar(100) not null,
  fantasy_league_type_id int not null,
  season int not null,
  scoring_system_id int not null,
  version int,
  foreign key (user_id) references users(id),
  foreign key (fantasy_league_type_id) references fantasy_league_types(id),
  foreign key (scoring_system_id) references scoring_systems(id)
);

/*
 * Model for the individual players on a fantasy roster.  This is just a join
 * table for the fantasy team entry and the player entry.
 */
drop table fantasy_team_players;
create table fantasy_team_players (
  id int auto_increment primary key,
  player_id int not null,
  fantasy_team_id int not null,
  version int,
  foreign key (player_id) references players(id),
  foreign key (fantasy_team_id) references fantasy_teams(id)
);

/*
 * Structure for storing scoring rules in the database.  For reusability, we put
 * rules in their own table and associate them with 0..n scoring systems through
 * the scoring_system_rules join table.
 */
drop table scoring_rules;
create table scoring_rules (
  id int auto_increment primary key,
  stat_key int not null,
  multiplier double not null,
  version int
);
create index stat_key_idx on scoring_rules(stat_key);

drop table scoring_systems;
create table scoring_systems (
  id int auto_increment primary key,
  name varchar (100) not null,
  version int
);

drop table scoring_system_rules;
create table scoring_system_rules (
  id int auto_increment primary key,
  scoring_rule_id int not null,
  scoring_system_id int not null,
  version int,
  foreign key (scoring_rule_id) references scoring_rules(id),
  foreign key (scoring_system_id) references scoring_systems(id)
);