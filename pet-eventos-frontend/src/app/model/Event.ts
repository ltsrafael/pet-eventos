import { User } from './User';
import { Attendance } from './Attendance';
import { Section } from './Section';
export class Event {
  idEvent: number;
  name: String;
  sections: Section[];
  users: User[]
  description: String;
  place: String;
  isActive: boolean;
  active: boolean;
  cancelled: boolean;
  isCancelled: boolean;
  attendances: Attendance[];
}
