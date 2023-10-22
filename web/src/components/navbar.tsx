import Logo from './logo.tsx';
import NavbarItem from './navbar-item.tsx';
import ScheduleIcon from '../assets/icons/schedule.svg';
import BarChartIcon from '../assets/icons/bar-chart.svg';
import SmartToyIcon from '../assets/icons/smart-toy.svg';

const Navbar = () => {
  return (
    <div
      id="navbar"
      className="flex h-16 w-full items-center justify-center bg-primaryColor sm:justify-start">
      <Logo />
      <div className="hidden h-16 sm:flex">
        <NavbarItem
          icon={ScheduleIcon}
          pageName="Delay prediction"
          route="/"
        />
        <NavbarItem
          icon={BarChartIcon}
          pageName="Statistics"
          route="statistics"
        />
        <NavbarItem
          icon={SmartToyIcon}
          pageName="Model"
          route="model"
        />
      </div>
    </div>
  );
};

export default Navbar;
