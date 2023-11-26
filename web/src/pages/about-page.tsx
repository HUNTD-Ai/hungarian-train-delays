const AboutPage = () => {
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
          About us
        </span>
      </div>
      <div
        id="about-page-content"
        className="flex w-full flex-col gap-y-4 pb-8 sm:gap-y-8">
        <p>
          As frequent users of public transport systems, we find delays
          inconvenient. However delays are to some degree unavoidable. No system
          can operate without mistakes, let alone one that spans a whole country
          with countless physical locations. Inherent difficulties aside, we
          have noticed patterns in these delays, just by using them frequently.
          This got us wandering if we could avoid the routes or trains that are
          consistently delayed. This lead us on a journey to create a model that
          could predict these delays.
        </p>
        <p>
          To develop the model, we had to dig deep into the data, which gave us
          unique insight into the realities of train delays. We decided to
          incorporate some of these enlightening statistics into our
          application. Although our initial motivation was selfish, we hope to
          provide our users (and maybe even the infrastructure operators) these
          same insights, to better understand the problems and hopefully be able
          to identify the potential weak points of the system.
        </p>

        <div>
          Contributors:
          <ul className="list-inside list-disc">
            <li>
              Ádám Kapus{' '}
              <a
                href="https://github.com/adamkapus"
                className="text-blue-500">
                @adamkapus
              </a>
            </li>
            <li>
              Martin Lényi{' '}
              <a
                href="https://github.com/tuku13"
                className="text-blue-500">
                @tuku13
              </a>
            </li>
            <li>
              Máté Debreczeni{' '}
              <a
                href="https://github.com/jazzysnake"
                className="text-blue-500">
                @jazzysnake
              </a>
            </li>
          </ul>
        </div>

        <p className="italic">
          This project is open source, you can find the source code{' '}
          <a
            className="text-blue-500"
            href="https://github.com/HUNTD-Ai/hungarian-train-delays/">
            [here]
          </a>
        </p>
      </div>
    </div>
  );
};

export default AboutPage;
