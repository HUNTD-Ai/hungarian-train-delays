import NavbarItem from './navbar-item.tsx';
import ScheduleIcon from '../assets/icons/schedule.svg';
import BarChartIcon from '../assets/icons/bar-chart.svg';
import SmartToyIcon from '../assets/icons/smart-toy.svg';

const BottomBar = () => {
  return (
    <div className="flex w-full items-center bg-primaryColor sm:hidden">
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
  );
};

export default BottomBar;
