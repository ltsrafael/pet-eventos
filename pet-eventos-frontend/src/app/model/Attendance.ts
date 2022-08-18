import { Event } from './Event';
import { User } from './User';
import { Section } from './Section';

export class Attendance {
  user: User = new User();
  event: Event = new Event();
  section: Section = new Section();
  isPresent: boolean;
}
