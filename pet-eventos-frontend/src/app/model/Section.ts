import { Attendance } from './Attendance';
import { User } from './User';
import { Event as Events} from './Event'

export class Section {
  idSection: number;
  startTime: String;
  endTime: String;
  users: User[];
  start: Date = new Date();
  end: Date = new Date();
  startDate: String;
  attendances: Attendance[];
  usersPresent: number[];
  event: Events;
}
