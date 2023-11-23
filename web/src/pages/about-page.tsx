type Chapter = {
  subTitle: string;
  paragraphs: Array<string>;
};

const AboutPage = () => {
  const chapters: Array<Chapter> = [
    {
      subTitle: 'Subtitle #1',
      paragraphs: [
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
      ],
    },
    {
      subTitle: 'Subtitle #2',
      paragraphs: [
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
      ],
    },
    {
      subTitle: 'Subtitle #3',
      paragraphs: [
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum',
      ],
    },
  ];

  return (
    <div
      id="about-page-container"
      className="flex h-full w-full flex-col items-center overflow-y-auto bg-backgroundLight px-8 text-textBoxTextColorLight dark:bg-backgroundDark dark:text-textColor sm:px-16">
      <div
        id="title-container"
        className="py-4 sm:py-8">
        <span
          id="title"
          className="text-lg font-bold sm:text-2xl">
          About the project
        </span>
      </div>
      <div
        id="chapter-list"
        className="flex w-full flex-col gap-y-4 pb-8 sm:gap-y-8">
        {chapters.map(chapter => (
          <div
            id={chapter.subTitle}
            className="flex flex-col gap-y-2 sm:gap-y-4">
            <span className="text-sm font-semibold sm:text-lg">
              {chapter.subTitle}
            </span>
            <div className="flex flex-col gap-y-1.5 sm:gap-y-2">
              {chapter.paragraphs.map((p, i) => (
                <p
                  id={'p#' + i}
                  className="text-sm sm:text-lg">
                  {p}
                </p>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AboutPage;
